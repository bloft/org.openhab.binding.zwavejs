package org.openhab.binding.zwavejs.model.message;

import org.openhab.binding.zwavejs.model.Message;

public class Version extends Message {
    public String driverVersion;
    public String serverVersion;
    public long homeId;
    public Integer minSchemaVersion;
    public Integer maxSchemaVersion;
}
