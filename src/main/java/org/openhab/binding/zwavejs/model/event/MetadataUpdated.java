package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.ValueId;

public class MetadataUpdated extends Event {
    private ValueId args;

    public ValueId getArgs() {
        return args;
    }
}
