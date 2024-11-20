package org.openhab.binding.zwavejs.handler;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.channel.ChannelDiscovery;
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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeHandler extends BaseThingHandler implements EventListener {
    private final Logger logger = LoggerFactory.getLogger(NodeHandler.class);
    private final ChannelTypeRegistry channelTypeRegistry;

    NodeConfig cfg;

    public NodeHandler(Thing thing, ChannelTypeRegistry channelTypeRegistry) {
        super(thing);
        this.channelTypeRegistry = channelTypeRegistry;
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

        try {
            getClient().isReady();

            Result result = getClient().sendCommand(new GetState(cfg.nodeId)).get();

            update(result.getResult(ResultWithState.class).state);

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
            updateStatus(ThingStatus.UNKNOWN);
        }

        for (Channel channel : getThing().getChannels()) {
            ValueId valueId = channel.getConfiguration().as(ChannelConfig.class).asValueId();
            logger.trace("{} : Channel: {} -> {}", cfg.nodeId, channel.getLabel(), valueId.getId());
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
            Result result = getClient().sendCommand(new GetValue(cfg.nodeId, valueId)).get();
            if(result.isSuccess()) {
                updateState(result.getResult(Value.class));
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
            refreshValue(valueId);
        } else {
            logger.info("{} : handleCommand: {} - {} - {}", cfg.nodeId, channelUID, command.getClass(), valueId.getId());
            if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_NUMBER)) {
                getClient().sendCommand(new SetValue(cfg.nodeId, valueId, command.toString()));
            } else if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_STRING)) {
                getClient().sendCommand(new SetValue(cfg.nodeId, valueId, command.toString()));
            } else if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_SWITCH)) {
                switch ((OnOffType) command) {
                    case ON:
                        getClient().sendCommand(new SetValue(cfg.nodeId, valueId, true));
                        break;
                    case OFF:
                        getClient().sendCommand(new SetValue(cfg.nodeId, valueId, false));
                        break;
                }
            }
        }
    }

    private void updateState(Value value) {
        if(value.getValue() == null) return;
        for (Channel channel : getThing().getChannels()) {
            if(channel.getConfiguration().as(ChannelConfig.class).equals(value)) {
                logger.info("{} : Channel update: {} : {}", cfg.nodeId, channel.getUID(), value.getValue());
                if(channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_NUMBER)) {
                    updateState(channel.getUID(), DecimalType.valueOf(value.getValue().toString()));
                } else if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_STRING)) {
                    updateState(channel.getUID(), StringType.valueOf(value.getValue().toString()));
                } else if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_CONTACT)) {
                    if(value.getValue() instanceof Boolean) {
                        updateState(channel.getUID(), ((boolean)value.getValue()) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
                    } else {
                        String newValue = value.getValue().toString();
                        if(newValue.equals(channel.getConfiguration().as(ChannelConfig.class).on)) {
                            updateState(channel.getUID(), OpenClosedType.OPEN);
                        } else if(newValue.equals(channel.getConfiguration().as(ChannelConfig.class).off)) {
                            updateState(channel.getUID(), OpenClosedType.CLOSED);
                        } else {
                            logger.warn("Unknown value for {} - {}", channel.getUID(), newValue);
                        }
                    }
                } else if (channel.getChannelTypeUID().equals(BindingConstants.CHANNEL_SWITCH)) {
                    updateState(channel.getUID(), ((boolean)value.getValue()) ? OnOffType.ON : OnOffType.OFF);
                } else {
                    logger.info("{} : {} unknown kind {}", cfg.nodeId, channel.getUID(), channel.getChannelTypeUID());
                }
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

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect() {}
}
