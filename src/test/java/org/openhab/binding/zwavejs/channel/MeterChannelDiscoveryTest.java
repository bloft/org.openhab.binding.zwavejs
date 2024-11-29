package org.openhab.binding.zwavejs.channel;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.CommandClass;
import org.openhab.binding.zwavejs.model.ValueId;
import org.openhab.binding.zwavejs.model.result.ValueMetadata;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ThingUID;

import static org.junit.jupiter.api.Assertions.*;

public class MeterChannelDiscoveryTest {

    public ValueMetadata metadata(String type, String unit, String label) {
        ValueMetadata valueMetadata = new ValueMetadata();
        valueMetadata.type = type;
        valueMetadata.unit = unit;
        valueMetadata.label = label;
        return valueMetadata;
    }

    @Test
    public void test() {
        MeterChannelDiscovery channelDiscovery = new MeterChannelDiscovery();
        ValueId value = new ValueId(0, CommandClass.METER, "value", "1234");
        value.setMetadata(metadata("number", "A", "Current"));

        Channel channel = channelDiscovery.of(new ThingUID(BindingConstants.NODE_TYPE, "test"), value);
        assertNotNull(channel);
        assertEquals("Number:ElectricCurrent", channel.getAcceptedItemType());
        assertEquals(BindingConstants.CHANNEL_METER, channel.getChannelTypeUID());
        assertEquals("Current", channel.getLabel());
    }

}