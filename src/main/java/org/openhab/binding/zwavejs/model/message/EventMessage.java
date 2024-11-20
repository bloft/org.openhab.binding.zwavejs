package org.openhab.binding.zwavejs.model.message;

import org.openhab.binding.zwavejs.model.Message;
import org.openhab.binding.zwavejs.model.Event;

public class EventMessage extends Message {
    private Event event;

    public Event getEvent() {
        return event;
    }
}
