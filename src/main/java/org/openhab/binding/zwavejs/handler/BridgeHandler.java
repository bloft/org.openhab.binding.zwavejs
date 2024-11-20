package org.openhab.binding.zwavejs.handler;

import org.openhab.binding.zwavejs.config.BridgeConfig;
import org.openhab.binding.zwavejs.internal.EventListener;
import org.openhab.binding.zwavejs.internal.ZWaveJSClient;
import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.command.controller.GetState;
import org.openhab.binding.zwavejs.model.event.Ready;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.binding.zwavejs.model.result.ControllerState;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class BridgeHandler extends BaseBridgeHandler implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(BridgeHandler.class);

    private BridgeConfig cfg;

    ZWaveJSClient client = null;

    public BridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.info("initialize " + getThing());

        try {
            cfg = getConfigAs(BridgeConfig.class);

            client = new ZWaveJSClient(cfg.getURI(), scheduler);
            client.register(this);
            client.connect();

            logger.info("Called connect on client");

            client.isReady();

            Result result = client.sendCommand(new GetState()).get();
            if(result.isSuccess()) {
                ControllerState state = result.getResult(ControllerState.class);
                updateProperty("firmwareVersion", state.firmwareVersion);
                updateProperty("sdkVersion", state.sdkVersion);
            }

        } catch (URISyntaxException e) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.CONFIGURATION_ERROR);
        } catch (Exception e) {
            logger.error("Unknown error", e);
        }
    }

    @Override
    public void dispose() {
        logger.info("dispose " + getThing());
        if(client != null) {
            client.unregister(this);
            client.dispose();
        }
    }

    public ZWaveJSClient getClient() {
        return client;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {}

    @Override
    public void onEvent(Event event) {
        if(event instanceof Ready) {
            //ThingDiscoveryService discovery = ThingDiscoveryService.getInstance();
        }
    }

    @Override
    public void onConnect() {
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void onDisconnect() {
        updateStatus(ThingStatus.OFFLINE);
    }
}
