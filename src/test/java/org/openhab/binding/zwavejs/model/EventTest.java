package org.openhab.binding.zwavejs.model;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.model.event.MetadataUpdated;
import org.openhab.binding.zwavejs.model.event.NodeStatusEvent;
import org.openhab.binding.zwavejs.model.event.Ready;
import org.openhab.binding.zwavejs.model.event.ValueUpdated;
import org.openhab.binding.zwavejs.model.message.EventMessage;
import org.openhab.binding.zwavejs.model.result.DeviceStatus;
import org.openhab.binding.zwavejs.model.result.NodeState;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest extends AbstractModelTest {
    @Test
    public void testMetadataUpdated() {
        EventMessage result = fromClasspath("metadata_updated.json", EventMessage.class);
        assertInstanceOf(MetadataUpdated.class, result.getEvent());
        MetadataUpdated update = (MetadataUpdated)result.getEvent();
        assertEquals(7, update.getNodeId());
        assertNotNull(update.getArgs().getMetadata());
        assertEquals("number", update.getArgs().getMetadata().getType());
        assertEquals("Electric Consumption [A]", update.getArgs().getMetadata().getLabel());
    }

    @Test
    void testNodeStatus() {
        EventMessage result = fromClasspath("dead.json", EventMessage.class);
        assertInstanceOf(NodeStatusEvent.class, result.getEvent());
        NodeStatusEvent status = (NodeStatusEvent)result.getEvent();
        assertEquals(5, status.getNodeId());
        assertEquals(DeviceStatus.DEAD, status.getStatus());

        result = fromClasspath("alive.json", EventMessage.class);
        assertTrue(NodeStatusEvent.class.isInstance(result.getEvent()));
        status = (NodeStatusEvent)result.getEvent();
        assertEquals(5, status.getNodeId());
        assertEquals(DeviceStatus.ALIVE, status.getStatus());
    }

    @Test
    void testReady() {
        EventMessage result = fromClasspath("ready.json", EventMessage.class);
        assertInstanceOf(Ready.class, result.getEvent());
        NodeState node = ((Ready)result.getEvent()).getNodeState();
        assertEquals(5, node.getNodeId());
        assertEquals(DeviceStatus.ALIVE, node.getStatus());
        assertEquals(true, node.isReady());
        assertEquals("QMSW-0A1P8", node.getDeviceConfig().getLabel());
        assertEquals("Wave 1PM Mini", node.getDeviceConfig().getDescription());
        assertEquals("Shelly Europe Ltd.", node.getDeviceConfig().getManufacturer());
        assertEquals(50, node.getValues().size());
    }

    @Test
    void testValueUpdated() {
        EventMessage result = fromClasspath("value_updated.json", EventMessage.class);
        assertInstanceOf(ValueUpdated.class, result.getEvent());
        Value value = ((ValueUpdated) result.getEvent()).getArgs();
        assertEquals(227.54, value.getValue());
    }
}
