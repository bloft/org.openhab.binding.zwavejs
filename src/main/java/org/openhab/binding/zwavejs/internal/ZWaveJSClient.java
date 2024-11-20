package org.openhab.binding.zwavejs.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.openhab.binding.zwavejs.model.Command;
import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.Message;
import org.openhab.binding.zwavejs.model.command.SetApiSchema;
import org.openhab.binding.zwavejs.model.command.StartListening;
import org.openhab.binding.zwavejs.model.message.EventMessage;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.binding.zwavejs.model.message.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


public class ZWaveJSClient {
    private final Logger logger = LoggerFactory.getLogger(ZWaveJSClient.class);

    private static final int CONNECT_TIMEOUT_MS = 1000;

    private URI uri;
    private WebSocketClient client;
    private ZWaveJSSocket socket;
    private ScheduledFuture<?> socketReconnect;
    private ScheduledExecutorService scheduler;
    private boolean isShutDown = false;

    private CountDownLatch ready = new CountDownLatch(1);

    private AtomicLong messageId = new AtomicLong(0);

    private CopyOnWriteArraySet<EventListener> listeners = new CopyOnWriteArraySet<>();

    private CopyOnWriteArraySet<ResultFetcher> resultFetchers = new CopyOnWriteArraySet<>();

    @NotNull
    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public ZWaveJSClient(URI uri, ScheduledExecutorService scheduler) {
        this.uri = uri;
        client = new WebSocketClient();
        socket = new ZWaveJSSocket();

        this.scheduler = scheduler;
    }

    public ZWaveJSClient(URI uri) {
        this(uri, Executors.newScheduledThreadPool(1));
    }

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    public void connect() {
        logger.info("Connecting to " + uri);
        try {
            ready = new CountDownLatch(1);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.setConnectTimeout(CONNECT_TIMEOUT_MS);
            client.start();
            client.connect(socket, uri, request);
        } catch (Exception e) {
            logger.warn("Could not connect webSocket message {}", e.getMessage());
        }
    }

    public void isReady() {
        try {
            ready.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void dispose() {
        isShutDown = true;
        try {
            logger.info("Stopping Z-Wave JS client");
            client.stop();
            ScheduledFuture<?> mySocketReconnect = socketReconnect;
            if (mySocketReconnect != null) {
                mySocketReconnect.cancel(true);
            }
        } catch (Exception e) {
            logger.warn("Could not stop webSocketClient,  message {}", e.getMessage());
        }
    }

    public synchronized Future<Result> sendCommand(Command command) {
        try {
            String msgId = Long.toString(messageId.incrementAndGet());
            command.setMessageId(msgId);
            String cmd = getMapper().writeValueAsString(command);

            ResultFetcher fetcher = new ResultFetcher(msgId);
            resultFetchers.add(fetcher);

            logger.debug("Sending command [{}] {}", command.getMessageId(), command.getCommand());

            socket.sendMessage(cmd);

            return scheduler.submit(fetcher);
        } catch (IOException e) {
            logger.warn("Could not send command {}", e.getMessage());
        }
        return null;
    }

    @WebSocket
    protected class ZWaveJSSocket {
        private Session session;

        public void sendMessage(String message) throws IOException {
            logger.trace("Sending message {}", message);
            session.getRemote().sendString(message);
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            logger.info("Connected {}", session.getRemoteAddress());
            session.getPolicy().setMaxTextMessageSize(1024 * 1024);
            this.session = session;

            sendCommand(new SetApiSchema(35));
            sendCommand(new StartListening());

            listeners.forEach(eventListener -> scheduler.execute(eventListener::onConnect));
        }

        @OnWebSocketMessage
        public void onMessage(String msg) {
            logger.trace("Received message {}", msg);
            try {
                Message response = getMapper().readValue(msg, Message.class);
                if (response instanceof Version version) {
                    logger.info("driverVersion: {}", version.driverVersion);
                    logger.info("serverVersion: {}", version.serverVersion);
                    ready.countDown();
                } else if (response instanceof EventMessage eventMessage) {
                    Event event = eventMessage.getEvent();
                    listeners.forEach(eventListener -> {
                        try {
                            scheduler.execute(() -> eventListener.onEvent(event));
                        } catch (Exception e) {
                            logger.warn("Unknown error", e);
                        }
                    });
                } else if (response instanceof Result result) {
                    logger.trace("Result for command {}", result.getMessageId());
                    resultFetchers.forEach(resultFetcher -> {
                        try {
                            resultFetcher.onResult(result);
                        } catch (Exception e) {
                            logger.warn("Unknown error", e);
                        }
                    });
                } else {
                    logger.debug("unknown response {}", msg);
                }
            } catch (InvalidTypeIdException e) {
                logger.warn("Unknown message {}", msg);
            } catch (Exception e) {
                logger.warn("Unexpected exception: {}", e.getMessage());
            }
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            logger.info("Connection closed: {} - {}", statusCode, reason);
            listeners.forEach(eventListener -> scheduler.execute(eventListener::onDisconnect));
            reconnect();
        }

        @OnWebSocketError
        public void onError(Throwable cause) {
            logger.error("onError " + cause.getMessage(), cause);
            for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
                logger.warn("{}: {}", stackTraceElement.getClassName(), stackTraceElement.getLineNumber());
            }
            listeners.forEach(eventListener -> scheduler.execute(eventListener::onDisconnect));
            reconnect();
        }

        private void reconnect() {
            if (!isShutDown) {
                logger.info("weSocket Closed - reconnecting");
                ScheduledFuture<?> mySocketReconnect = socketReconnect;
                if (mySocketReconnect == null || mySocketReconnect.isDone()) {
                    socketReconnect = scheduler.schedule(ZWaveJSClient.this::connect, 10, TimeUnit.SECONDS);
                }
            }
        }
    }

    public class ResultFetcher implements Callable<Result> {
        private final String messageId;
        private Result result;
        private final CountDownLatch latch = new CountDownLatch(1);

        public ResultFetcher(String messageId) {
            this.messageId = messageId;
        }

        public boolean onResult(Result result) {
            if(result.getMessageId().equals(messageId)) {
                this.result = result;
                latch.countDown();
                resultFetchers.remove(this);
                return true;
            }
            return false;
        }

        @Override
        public Result call() throws Exception {
            latch.await(10, TimeUnit.SECONDS);
            return result;
        }
    }

}
