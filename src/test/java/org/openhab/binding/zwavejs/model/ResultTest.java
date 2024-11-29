package org.openhab.binding.zwavejs.model;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.binding.zwavejs.model.result.*;
import org.openhab.core.thing.ThingStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultTest extends AbstractModelTest {
    @Test
    public void testValueIds() {
        Result result = fromClasspath("valueIds.json", Result.class);
        assertEquals("2", result.getMessageId());
        ValueIds valueIds = result.getResult(ValueIds.class);
        assertEquals(50, valueIds.valueIds.size());
        valueIds.valueIds.stream()
                .filter(valueId -> valueId.getCommandClass().equals(CommandClass.BINARY_SWITCH))
                .forEach(valueId -> System.out.println(valueId.getId()));
    }

    @Test
    public void testValueMetadata() {
        Result result = fromClasspath("value_metadata.json", Result.class);
        assertEquals("2", result.getMessageId());
        ValueMetadata meta = result.getResult(ValueMetadata.class);
        assertEquals("Current value", meta.getLabel());
        assertEquals("boolean", meta.getType());
    }

    @Test
    public void testNodeState() {
        Result result = fromClasspath("node_state.json", Result.class);
        assertEquals("3", result.getMessageId());
        NodeState state = result.getResult(ResultWithState.class).state;
        assertEquals(5, state.getNodeId());
        assertEquals(true, state.isReady());
        assertEquals(DeviceStatus.ALIVE, state.getStatus());
        assertEquals(ThingStatus.ONLINE, state.getStatus().getStatus());
        assertEquals("QMSW-0A1P8", state.getDeviceConfig().getLabel());

        state.getValues().stream()
                .filter(valueId -> valueId.getCommandClass().equals(CommandClass.VERSION))
                .forEach(valueId -> System.out.format("%s = %s\n", valueId.getPropertyName(), valueId.getValue()));
    }

    @Test
    public void testNodeState2() {
        Result result = fromClasspath("node_state2.json", Result.class);
        assertEquals("3", result.getMessageId());
        NodeState state = result.getResult(ResultWithState.class).state;
        assertEquals(5, state.getNodeId());
        assertEquals(false, state.isReady());
        assertEquals(DeviceStatus.DEAD, state.getStatus());
        assertEquals(ThingStatus.OFFLINE, state.getStatus().getStatus());
        assertEquals("QMSW-0A1P8", state.getDeviceConfig().getLabel());

        state.getValues().stream()
                .filter(valueId -> valueId.getCommandClass().equals(CommandClass.VERSION))
                .forEach(valueId -> System.out.format("%s = %s\n", valueId.getPropertyName(), valueId.getValue()));
    }
}
