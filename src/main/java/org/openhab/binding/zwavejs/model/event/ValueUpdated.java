package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.Value;

public class ValueUpdated extends Event {
    private Value args;

    public Value getArgs() {
        return args;
    }
}
