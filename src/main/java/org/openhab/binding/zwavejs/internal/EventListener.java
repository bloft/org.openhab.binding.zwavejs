package org.openhab.binding.zwavejs.internal;

import org.openhab.binding.zwavejs.model.Event;

public interface EventListener {
    void onEvent(Event event);
    void onConnect();
    void onDisconnect();
}
