package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;

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

    private Channel of(ThingUID thing, ValueId valueId) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(getSimpleType(valueId.getMetadata())).
                withLabel(valueId.getMetadata().getLabel()).
                withConfiguration(configOf(valueId)).
                build();
    }
}
