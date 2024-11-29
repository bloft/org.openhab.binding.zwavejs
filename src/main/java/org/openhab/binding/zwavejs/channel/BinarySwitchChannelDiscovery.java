package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class BinarySwitchChannelDiscovery implements ChannelDiscovery {
    @Override
    public List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds) {
        return valuesIds.stream()
                .filter(valueId -> valueId.getCommandClass() == CommandClass.BINARY_SWITCH)
                .collect(Collectors.groupingBy(ValueId::getEndpoint, Collectors.toMap(ValueId::getProperty, valueId -> valueId)))
                .values().stream()
                .map(map -> of(thing, "Switch", map.get("currentValue"), map.getOrDefault("targetValue", map.get("currentValue"))))
                .collect(Collectors.toList());
    }

    private Configuration configOf(ValueId valueId, ValueId targetValueId) {
        Configuration cfg = configOf(valueId);
        cfg.put("targetProperty", targetValueId.getProperty());
        cfg.put("targetPropertyKey", targetValueId.getPropertyKey());
        return cfg;
    }

    private Channel of(ThingUID thing, String label, ValueId valueId, ValueId targetValueId) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(BindingConstants.CHANNEL_BINARY_SWITCH).
                withAcceptedItemType(itemType(valueId.getMetadata())).
                withLabel(valueId.getLabel(label)).
                withConfiguration(configOf(valueId, targetValueId)).
                build();
    }

}
