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
            new BinarySwitchChannelDiscovery(),
            new ColorChannelDiscovery(),
            new MultilevelChannelDiscovery(),
            new MeterChannelDiscovery(),
            new NotificationChannelDiscovery(),
            new BatteryChannelDiscovery(),
    };

    List<? extends Channel> getChannels(ThingUID thing, List<? extends ValueId> valuesIds);

    static List<Channel> discover(ThingUID thing, List<? extends ValueId> valuesIds) {
        return discover(thing, valuesIds, ALL_DISCOVERY);
    }

    static List<Channel> discover(ThingUID thing, List<? extends ValueId> valuesIds, ChannelDiscovery ... discoveries) {
        return Arrays.stream(discoveries).flatMap(d -> d.getChannels(thing, valuesIds).stream()).collect(Collectors.toList());
    }

    default String itemType(ValueMetadata metadata) {
        switch(metadata.getType()) {
            case "number":
                if(metadata.getUnit() != null) {
                    switch (metadata.getUnit()) {
                        case "kWh": return "Number:Energy";
                        case "W": return "Number:Power";
                        case "V":;
                        case "mV": return "Number:ElectricPotential";
                        case "A":;
                        case "mA": return "Number:ElectricCurrent";
                        case "%": return "Number:Dimensionless";
                        case "°C": return "Number:Temperature";
                        case "Lux": return "Number:Illuminance";
                        case "mmHg":
                        case "kPa": return "Number:Pressure";
                        case "cm":
                        case "m": return "Number:Length";
                        case "kg": return "Number:Mass";
                    }
                }
                return "Number";
            case "string": return "String";
            case "boolean": return "Switch";
            case "color": return "Color";
            default:  return null;
        }
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
            case "boolean" : return BindingConstants.CHANNEL_SWITCH;
            case "color" : return BindingConstants.CHANNEL_COLOR;
            default: throw new RuntimeException("Unknown type");
        }
    }
}
