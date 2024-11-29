package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;

import java.util.List;
import java.util.Map;

public class NotificationChannelDiscovery implements ChannelDiscovery {
    @Override
    public List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds) {
        return valuesIds.stream()
                .filter(valueId -> valueId.getCommandClass() == CommandClass.NOTIFICATION)
                .filter(valueId -> valueId.getMetadata() != null)
                .filter(valueId -> valueId.getMetadata().getStates() != null)
                .filter(valueId -> valueId.getMetadata().getType().equals("number"))
                .map(valueId -> {
                    if(valueId.getMetadata().getStates().size() == 1 || valueId.getMetadata().getStates().size() == 2) {
                        Map states = valueId.getMetadata().getStates();
                        String key = states.keySet().stream().filter(o -> !o.equals("0")).findAny().get().toString();
                        return of(thing, valueId, BindingConstants.CHANNEL_NOTIFICATION, configOf(valueId, key, "0"));
                    }
                    return of(thing, valueId, BindingConstants.CHANNEL_NUMBER, configOf(valueId));
                }).toList();
    }

    public Configuration configOf(ValueId valueId, String on, String off) {
        Configuration cfg = configOf(valueId);
        cfg.put("on", on);
        cfg.put("off", off);
        return cfg;
    }

    private Channel of(ThingUID thing, ValueId valueId, ChannelTypeUID type, Configuration cfg) {
        return ChannelBuilder.create(new ChannelUID(thing, valueId.getId())).
                withType(type).
                withAcceptedItemType(type.equals(BindingConstants.CHANNEL_NUMBER) ? "Number" : "Switch").
                withLabel(valueId.getLabel()).
                withConfiguration(cfg).
                build();
    }
}
