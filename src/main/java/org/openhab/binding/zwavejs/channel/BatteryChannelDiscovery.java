package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;

import java.util.List;
import java.util.stream.Collectors;

public class BatteryChannelDiscovery implements ChannelDiscovery {
    @Override
    public List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds) {
        return valuesIds.stream()
                .filter(valueId -> valueId.getCommandClass() == CommandClass.BATTERY)
                .filter(valueId -> valueId.getMetadata() != null)
                .filter(valueId -> valueId.getMetadata().isReadable())
                .map(valueId -> of(thing, valueId))
                .collect(Collectors.toList());
    }

    private ChannelTypeUID getType(ValueId valueId) {
        switch(valueId.getProperty()) {
            case "level": return BindingConstants.CHANNEL_BATTERY_LEVEL;
            case "isLow": return BindingConstants.CHANNEL_BATTERY_LOW;
            default: return getSimpleType(valueId.getMetadata());
        }
    }

    private Channel of(ThingUID thing, ValueId valueId) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(getType(valueId)).
                withAcceptedItemType(itemType(valueId.getMetadata())).
                withLabel(valueId.getLabel()).
                withConfiguration(configOf(valueId)).
                build();
    }
}
