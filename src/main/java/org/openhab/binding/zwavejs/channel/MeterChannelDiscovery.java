package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class MeterChannelDiscovery implements ChannelDiscovery {
    private final Logger logger = LoggerFactory.getLogger(MeterChannelDiscovery.class);

    @Override
    public List<Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds) {
        return valuesIds.stream()
                .filter(valueId -> valueId.getCommandClass() == CommandClass.METER)
                .filter(valueId -> valueId.getMetadata() != null)
                .map(valueId -> of(thing, valueId))
                .collect(Collectors.toList());
    }

    private ChannelTypeUID getType(ValueId valueId) {
        if(valueId.getProperty().equals("value") && valueId.getPropertyKey() != null) {
            return BindingConstants.CHANNEL_METER;
        }
        return getSimpleType(valueId.getMetadata());
    }

    protected Channel of(ThingUID thing, ValueId valueId) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(getType(valueId)).
                withAcceptedItemType(itemType(valueId.getMetadata())).
                withLabel(valueId.getLabel()).
                withConfiguration(configOf(valueId)).
                build();
    }
}
