package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.result.DeviceStatus;
import org.openhab.binding.zwavejs.model.Event;

public abstract class NodeStatusEvent extends Event {
    public abstract DeviceStatus getStatus();
}
