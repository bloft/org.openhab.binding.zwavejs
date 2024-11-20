package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.openhab.binding.zwavejs.model.event.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "event", visible = true, defaultImpl = Event.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dead.class, name = "dead"),
        @JsonSubTypes.Type(value = Alive.class, name = "alive"),
        @JsonSubTypes.Type(value = Awake.class, name = "awake"),
        @JsonSubTypes.Type(value = Asleep.class, name = "asleep"),
        @JsonSubTypes.Type(value = ValueUpdated.class, names = {"value added","value updated"}),
        @JsonSubTypes.Type(value = MetadataUpdated.class, name = "metadata updated"),
        @JsonSubTypes.Type(value = Ready.class, name = "ready"),
})
public class Event {
    private String source;
    private Integer nodeId;
    private String event;

    public String getSource() {
        return source;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public String getEvent() {
        return event;
    }

    public String getDestination() {
        String dest = getSource();
        if(dest.equals("node") && getNodeId() != null) {
            dest += getNodeId();
        }
        return dest;
    }

    public String toString() {
        return String.format("Event[%s]: %s", getDestination(), getEvent());
    }
}
