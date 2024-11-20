package org.openhab.binding.zwavejs.model.result;

import org.openhab.binding.zwavejs.model.Value;
import org.openhab.binding.zwavejs.model.ValueId;

import java.util.List;

public class NodeState {
    private Integer nodeId;
    private DeviceStatus status;
    private boolean ready;
    private String firmwareVersion;
    private DeviceConfig deviceConfig;
    private List<Value> values;

    public Integer getNodeId() {
        return nodeId;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public boolean isReady() {
        return ready;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public List<Value> getValues() {
        return values;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }
}
