package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
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

    private Channel of(ThingUID thing, ValueId valueId) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(getSimpleType(valueId.getMetadata())).
                withLabel(valueId.getMetadata().getLabel()).
                withConfiguration(configOf(valueId)).
                build();
    }
}
