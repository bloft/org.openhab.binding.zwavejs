package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.result.DeviceStatus;

public class Asleep extends NodeStatusEvent {
    @Override
    public DeviceStatus getStatus() {
        return DeviceStatus.ASLEEP;
    }
}
