package org.openhab.binding.zwavejs.model.result;

import com.fasterxml.jackson.annotation.JsonValue;
import org.openhab.core.thing.ThingStatus;

public enum DeviceStatus {
    UNKNOWN(0, ThingStatus.UNKNOWN),
    ASLEEP(1, ThingStatus.ONLINE),
    AWAKE(2, ThingStatus.ONLINE),
    DEAD(3, ThingStatus.OFFLINE),
    ALIVE(4 , ThingStatus.ONLINE);

    @JsonValue
    private final int id;

    private ThingStatus status;

    DeviceStatus(int id, ThingStatus status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public ThingStatus getStatus() {
        return status;
    }
}
