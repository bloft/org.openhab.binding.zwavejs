package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.openhab.binding.zwavejs.model.message.EventMessage;
import org.openhab.binding.zwavejs.model.message.Result;
import org.openhab.binding.zwavejs.model.message.Version;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Version.class, name = "version"),
        @JsonSubTypes.Type(value = Result.class, name = "result"),
        @JsonSubTypes.Type(value = EventMessage.class, name = "event"),
})
public abstract class Message {
}
