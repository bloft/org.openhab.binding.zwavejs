package org.openhab.binding.zwavejs.model;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.model.event.NodeStatusEvent;
import org.openhab.binding.zwavejs.model.event.Ready;
import org.openhab.binding.zwavejs.model.event.ValueUpdated;
import org.openhab.binding.zwavejs.model.message.EventMessage;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.binding.zwavejs.model.message.Version;
import org.openhab.binding.zwavejs.model.result.DeviceStatus;
import org.openhab.binding.zwavejs.model.result.NodeState;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest extends AbstractModelTest {
    @Test
    void testResult() {
        Result result = fromClasspath("result.json", Result.class);
        assertEquals(true, result.isSuccess());
        assertEquals("1", result.getMessageId());
    }

    @Test
    void testVersion() {
        Version result = fromClasspath("version.json", Version.class);
        assertEquals("12.5.6", result.driverVersion);
        assertEquals("1.35.0-beta.1", result.serverVersion);
        assertEquals(4258903659L, result.homeId);
        assertEquals(0, result.minSchemaVersion);
        assertEquals(35, result.maxSchemaVersion);
    }

    @Test
    void testEvent() {
        EventMessage result = fromClasspath("event.json", EventMessage.class);
        assertInstanceOf(ValueUpdated.class, result.getEvent());
        assertEquals(7, result.getEvent().getNodeId());
        assertEquals("node", result.getEvent().getSource());
        assertEquals("value updated", result.getEvent().getEvent());
    }
}