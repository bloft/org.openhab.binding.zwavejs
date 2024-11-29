package org.openhab.binding.zwavejs.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openhab.binding.zwavejs.channel.ChannelDiscovery;
import org.openhab.binding.zwavejs.channel.ZWaveJSChannelTypeProvider;
import org.openhab.binding.zwavejs.config.ChannelConfig;
import org.openhab.binding.zwavejs.config.NodeConfig;
import org.openhab.binding.zwavejs.internal.ZWaveJSClient;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.internal.EventListener;
import org.openhab.binding.zwavejs.model.Value;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.binding.zwavejs.model.command.node.GetValue;
import org.openhab.binding.zwavejs.model.command.node.SetValue;
import org.openhab.binding.zwavejs.model.event.MetadataUpdated;
import org.openhab.binding.zwavejs.model.event.Ready;
import org.openhab.binding.zwavejs.model.result.NodeState;
import org.openhab.binding.zwavejs.model.result.ResultWithState;
import org.openhab.binding.zwavejs.model.command.node.GetState;
import org.openhab.binding.zwavejs.model.event.NodeStatusEvent;
import org.openhab.binding.zwavejs.model.event.ValueUpdated;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.core.library.types.*;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NodeHandler extends BaseThingHandler implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(NodeHandler.class);
    private ZWaveJSChannelTypeProvider channelTypeProvider;

    NodeConfig cfg;

    public NodeHandler(Thing thing, ZWaveJSChannelTypeProvider channelTypeProvider) {
        super(thing);
        this.channelTypeProvider = channelTypeProvider;
    }

    private BridgeHandler getBridgeHandler() {
        return (BridgeHandler) Optional.of(super.getBridge()).orElseThrow().getHandler();
    }

    private ZWaveJSClient getClient() {
        return getBridgeHandler().getClient();
    }

    @Override
    public void initialize() {
        cfg = getConfigAs(NodeConfig.class);

        logger.info("{} : initialize {}", cfg.nodeId, getThing());

        getClient().register(this);

        refresh();
    }

    private void refresh() {
        try {
            getClient().isReady();

            Result result = getClient().sendCommand(new GetState(cfg.nodeId)).get();

            update(result.getResult(ResultWithState.class).state);

        } catch (Exception e) {
            updateStatus(ThingStatus.UNKNOWN);
            logger.error("{} - Error: {}", cfg.nodeId, e.getMessage(), e);
        }
    }

    private void update(NodeState nodeState) {
        logger.debug("{} : update", cfg.nodeId);

        updateStatus(nodeState.getStatus().getStatus());

        if(nodeState.getDeviceConfig() != null) {
            updateProperty("label", nodeState.getDeviceConfig().getLabel());
            updateProperty("description", nodeState.getDeviceConfig().getDescription());
            updateProperty("manufacturer", nodeState.getDeviceConfig().getManufacturer());
            updateProperty("firmware_version", nodeState.getFirmwareVersion());
        }

        nodeState.getValues().stream()
                .filter(valueId -> valueId.getCommandClass().equals(CommandClass.VERSION))
                .forEach(valueId -> updateProperty(valueId.getPropertyName(), valueId.getValue().toString()));

        updateChannels(nodeState.getValues());

        nodeState.getValues().stream().forEach(this::updateState);
    }

    private void refreshValue(ValueId valueId) {
        logger.info("Refresh value of {}", valueId.getId());
        try {
            Result result = getClient().sendCommand(new GetValue(cfg.nodeId, valueId)).get(30, TimeUnit.SECONDS);
            if(result.isSuccess()) {
                logger.info("{} - Result: {}", cfg.nodeId, new ObjectMapper().writeValueAsString(result.getResult()));
                //updateState(result.getResult(Value.class));
            } else {
                logger.warn("{} : Failed to get value", cfg.nodeId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // https://github.com/openhab/openhab-addons/blob/main/bundles/org.openhab.binding.mqtt.homie/src/main/java/org/openhab/binding/mqtt/homie/internal/handler/HomieThingHandler.java
    private void updateChannels(List<? extends ValueId> valueIds) {
        Set<ChannelUID> channels = getThing().getChannels().stream().map(channel -> channel.getUID()).collect(Collectors.toSet());
        for (Channel channel : ChannelDiscovery.discover(getThing().getUID(), valueIds)) {
            if(!channels.contains(channel.getUID())) { // Only add new channels
                logger.info("{} : Added new chanel {}", cfg.nodeId, channel.getLabel());
                updateThing(editThing().withChannel(channel).build());
            }
        }
    }

    @Override
    public void dispose() {
        getClient().unregister(this);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        Channel channel = getThing().getChannel(channelUID);
        ValueId valueId = channel.getConfiguration().as(ChannelConfig.class).getTargetValueId();
        if(command instanceof RefreshType) {
            //refreshValue(valueId);
            refresh();
        } else {
            logger.info("{} : handleCommand: {} - {} - {}", cfg.nodeId, channelUID, command.getClass(), valueId.getId());

            switch (channel.getAcceptedItemType().split(":")[0]) {
                case "Number":
                case "String":
                    getClient().sendCommand(new SetValue(cfg.nodeId, valueId, command.toString()));
                    break;
                case "Switch":
                    switch ((OnOffType) command) {
                        case ON:
                            getClient().sendCommand(new SetValue(cfg.nodeId, valueId, true));
                            break;
                        case OFF:
                            getClient().sendCommand(new SetValue(cfg.nodeId, valueId, false));
                            break;
                    }
                    break;
                case "Color":
                    getClient().sendCommand(new SetValue(cfg.nodeId, valueId, hsbToHex((HSBType)command)));
                    break;
            }
        }
    }

    private void updateState(Value value) {
        if(value.getValue() == null) return;
        for (Channel channel : getThing().getChannels()) {
            if(channel.getConfiguration().as(ChannelConfig.class).equals(value)) {
                logger.info("{} : Channel update: {} : {} : {}", cfg.nodeId, channel.getUID(), channel.getAcceptedItemType(), value.getValue());

                switch (channel.getAcceptedItemType().split(":")[0]) {
                    case "Number":
                        updateState(channel.getUID(), DecimalType.valueOf(value.getValue().toString()));
                        break;
                    case "String":
                        updateState(channel.getUID(), StringType.valueOf(value.getValue().toString()));
                        break;
                    case "Switch":
                        updateBooleanState(channel, value.getValue(), OnOffType.ON, OnOffType.OFF);
                        break;
                    case "Contact":
                        updateBooleanState(channel, value.getValue(), OpenClosedType.OPEN, OpenClosedType.CLOSED);
                        break;
                    case "Color":
                        updateState(channel.getUID(), hexToHSB(value.getValue().toString()));
                        break;
                    case "Rollershutter":
                    case "Dimmer":
                    case "Location":
                    case "Player":
                        logger.warn("{} : Unsupported item type: ", cfg.nodeId, channel.getAcceptedItemType());
                        break;
                }
            }
        }
    }

    private HSBType hexToHSB(String hexCode) {
        int red = Integer.valueOf(hexCode.substring(0, 2), 16);
        int green = Integer.valueOf(hexCode.substring(2, 4), 16);
        int blue = Integer.valueOf(hexCode.substring(4, 6), 16);
        return HSBType.fromRGB(red, green, blue);
    }

    private String hsbToHex(HSBType value) {
        int[] rgb = ColorUtil.hsbToRgb(value);
        return String.format("%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    private void updateBooleanState(Channel channel, Object value, State onValue, State offValue) {
        if(value instanceof Boolean) {
            updateState(channel.getUID(), (boolean) value ? onValue : offValue);
        } else {
            String newValue = value.toString();
            if(newValue.equals(channel.getConfiguration().as(ChannelConfig.class).on)) {
                updateState(channel.getUID(), onValue);
            } else if(newValue.equals(channel.getConfiguration().as(ChannelConfig.class).off)) {
                updateState(channel.getUID(), offValue);
            } else {
                logger.warn("Unknown value for {} - {}", channel.getUID(), newValue);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        if("node".equals(event.getSource()) && event.getNodeId().equals(cfg.nodeId)) {
            logger.trace("{} : Event received: {}", cfg.nodeId, event.getEvent());
            if(event instanceof NodeStatusEvent e) {
                updateStatus(e.getStatus().getStatus());
            } else if (event instanceof Ready e) {
                update(e.getNodeState());
            } else if (event instanceof ValueUpdated e) {
                logger.debug("{} : Value updated: {}", cfg.nodeId, e.getArgs().getId());
                updateState(e.getArgs());
            } else if (event instanceof MetadataUpdated e) {
                logger.debug("{} : Metadata updated: {}", cfg.nodeId, e.getArgs().getId());
                updateChannels(List.of(e.getArgs()));
            } else {
                logger.debug("{} : Unknown event {} - {}", cfg.nodeId, event.getEvent(), event.getClass().getSimpleName());
                // Valid event
            }
        }
    }

    public ChannelType getChannelType(ChannelTypeUID uid) {
        for (ChannelType channelType : channelTypeProvider.getChannelTypes(Locale.getDefault())) {
            logger.info("{} : channelTypes: {}", cfg.nodeId, channelType.getUID());
            if(uid.equals(channelType.getUID())) {
                logger.info("{} : ChannelType found: {}", cfg.nodeId, channelType);
                return channelType;
            }
        }
        logger.warn("{} : Failed to find channel type for {}", cfg.nodeId, uid);
        return null;
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect() {}
}
