package org.openhab.binding.zwavejs.config;

import org.openhab.binding.zwavejs.model.ValueId;

import java.util.Objects;

public class ChannelConfig {
    public int endpoint;
    public int commandClass;
    public String property;
    public String propertyKey;

    public String on;
    public String off;

    public String targetProperty;
    public String targetPropertyKey;

    public boolean equals(ValueId valueId) {
        return Objects.equals(endpoint, valueId.getEndpoint()) &&
                Objects.equals(commandClass, valueId.getCommandClass().getId()) &&
                Objects.equals(property, valueId.getProperty()) &&
                Objects.equals(propertyKey, valueId.getPropertyKey());
    }

    public ValueId asValueId() {
        return new ValueId(endpoint, commandClass, property, propertyKey);
    }

    public ValueId getTargetValueId() {
        return new ValueId(
                endpoint,
                commandClass,
                targetProperty != null ? targetProperty : property,
                targetPropertyKey != null ? targetPropertyKey : propertyKey
        );
    }
}
