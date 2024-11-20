package org.openhab.binding.zwavejs.model.result;

import java.util.Map;

public class DeviceConfig {
    private String manufacturer;
    private String label;
    private String description;
    private Map<String, Object> metadata;

    public String getManufacturer() {
        return manufacturer;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
