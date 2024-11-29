package org.openhab.binding.zwavejs.channel;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.model.result.ValueMetadata;
import org.openhab.core.types.StateDescriptionFragment;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ThingChannelTypeProviderTest {
    @Test
    public void testCreateStateDescription() {
        ValueMetadata value = new ValueMetadata();
        value.min = 0;
        value.max = 10;
        value.unit = "kWh";
        value.states = Map.of(
                "0", "Test",
                "1", "Foo"
        );
        StateDescriptionFragment state = ZWaveJSChannelTypeProvider.createStateDescription(value);
        assertEquals(BigDecimal.valueOf(0), state.getMinimum());
        assertEquals(BigDecimal.valueOf(10), state.getMaximum());
        assertEquals(true, state.isReadOnly());
        System.out.println(state);
    }

    @Test
    public void testGetItemType() {
        ValueMetadata value = new ValueMetadata();
        value.type = "number";
        value.unit = "kWh";
        System.out.println(ZWaveJSChannelTypeProvider.getItemType(value));
    }
}