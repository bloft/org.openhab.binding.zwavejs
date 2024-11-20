package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.binding.zwavejs.model.result.ValueMetadata;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.type.ChannelTypeUID;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface ChannelDiscovery {
    ChannelDiscovery[] ALL_DISCOVERY = new ChannelDiscovery[]{
            new MeterChannelDiscovery(),
            new BatteryChannelDiscovery(),
            new MultilevelChannelDiscovery(),
            new NotificationChannelDiscovery(),
            new BinarySwitchChannelDiscovery(),
    };

    List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds);

    static List<Channel> discover(ThingUID thing, List<? extends ValueId> valuesIds) {
        return discover(thing, valuesIds, ALL_DISCOVERY);
    }

    static List<Channel> discover(ThingUID thing, List<? extends ValueId> valuesIds, ChannelDiscovery ... discoveries) {
        return Arrays.stream(discoveries).flatMap(d -> d.getChannels(thing, valuesIds).stream()).collect(Collectors.toList());
    }

    default Configuration configOf(ValueId valueId) {
        Configuration config = new Configuration();
        config.put("endpoint", valueId.getEndpoint());
        config.put("commandClass", valueId.getCommandClass().getId());
        config.put("property", valueId.getProperty());
        config.put("propertyKey", valueId.getPropertyKey());
        return config;
    }

    default ChannelTypeUID getSimpleType(ValueMetadata metadata) {
        switch(metadata.getType()) {
            case "number" : return BindingConstants.CHANNEL_NUMBER;
            case "string" : return BindingConstants.CHANNEL_STRING;
            case "boolean" :
                if(metadata.isWriteable()) {
                    return BindingConstants.CHANNEL_SWITCH;
                } else {
                    return BindingConstants.CHANNEL_CONTACT;
                }
            default: throw new RuntimeException("Unknown type");
        }
    }
}
