package org.openhab.binding.zwavejs.internal;

import ch.qos.logback.classic.Level;
import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.binding.zwavejs.model.result.ValueIds;
import org.openhab.binding.zwavejs.model.command.node.GetDefinedValueIds;
import org.openhab.binding.zwavejs.model.message.Result;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

class ZWaveJSClientTest {
    public static void main(String[] args) throws URISyntaxException, InterruptedException, ExecutionException {
        Logger logger = (Logger) LoggerFactory.getLogger(ZWaveJSClient.class);
        logger.setLevel(Level.INFO);

        ZWaveJSClient client = new ZWaveJSClient(new URI("ws://localhost:3000"));

        client.register(new EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(event.getEvent());
            }

            @Override
            public void onConnect() {
                System.out.println("Connected");
            }

            @Override
            public void onDisconnect() {
                System.out.println("Disconnected");
            }
        });

        client.connect();

        System.out.println("Sleeping");
        Thread.sleep(Duration.ofSeconds(10).toMillis());

        Result result = client.sendCommand(new GetDefinedValueIds(5)).get();
        if(result.isSuccess()) {
            for (ValueId valueId : result.getResult(ValueIds.class).valueIds) {
                System.out.println(valueId.getId());
            }
        }

        Thread.sleep(Duration.ofMinutes(5).toMillis());
        System.out.println("Stopping");
        client.dispose();
    }
}