package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.result.DeviceStatus;

public class Awake extends NodeStatusEvent {
    @Override
    public DeviceStatus getStatus() {
        return DeviceStatus.AWAKE;
    }
}
