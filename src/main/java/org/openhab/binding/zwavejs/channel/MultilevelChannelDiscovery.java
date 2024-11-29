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

public class MultilevelChannelDiscovery implements ChannelDiscovery {
    @Override
    public List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds) {
        return valuesIds.stream()
                .filter(valueId -> valueId.getCommandClass() == CommandClass.MULTILEVEL_SENSOR)
                .filter(valueId -> valueId.getMetadata() != null)
                .filter(valueId -> valueId.getMetadata().isReadable())
                .map(valueId -> of(thing, valueId))
                .collect(Collectors.toList());
    }

    private ChannelTypeUID getType(ValueId valueId) {
        String property = valueId.getProperty().toLowerCase();
        if(property.contains("temperature")) {
            return BindingConstants.CHANNEL_TEMPERATURE;
        } else if(property.contains("humidity")) {
            return BindingConstants.CHANNEL_HUMIDITY;
        } else if(property.contains("illuminance")) {
            return BindingConstants.CHANNEL_ILLUMINANCE;
        } else if(property.contains("ultraviolet")) {
            return BindingConstants.CHANNEL_ULTRAVIOLET;
        } else if(property.contains("pressure")) {
            return BindingConstants.CHANNEL_PRESSURE;
        } else {
            return getSimpleType(valueId.getMetadata());
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
