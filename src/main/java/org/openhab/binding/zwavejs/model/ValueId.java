package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openhab.binding.zwavejs.model.result.ValueMetadata;

public class ValueId {
    private int endpoint;
    private CommandClass commandClass;
    private String property;
    private String propertyName;
    private String propertyKey;
    private String propertyKeyName;

    private ValueMetadata metadata;

    public ValueId() {}

    public ValueId(int endpoint, CommandClass commandClass, String property, String propertyKey) {
        this(endpoint, commandClass.getId(), property, propertyKey);
    }

    public ValueId(int endpoint, int commandClassId, String property, String propertyKey) {
        this.endpoint = endpoint;
        this.commandClass = CommandClass.byId(commandClassId);
        this.property = property;
        this.propertyKey = propertyKey;
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append(endpoint);
        sb.append("-").append(commandClass.getId());
        sb.append("-").append(property.replace(" ", "_"));
        if(propertyKey != null) {
            sb.append("-").append(propertyKey.replace(" ", "_"));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getId();
    }

    public int getEndpoint() {
        return endpoint;
    }

    public CommandClass getCommandClass() {
        return commandClass;
    }

    public String getProperty() {
        return property;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getPropertyKeyName() {
        return propertyKeyName;
    }

    public ValueMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ValueMetadata metadata) {
        this.metadata = metadata;
    }

    public String getLabel(String label) {
        return label + (endpoint == 0 ? "" : " " + endpoint);
    }

    @JsonIgnore
    public String getLabel() {
        return getLabel(getMetadata().getLabel());
    }
}
