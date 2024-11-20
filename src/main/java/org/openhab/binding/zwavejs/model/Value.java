package org.openhab.binding.zwavejs.model;

public class Value extends ValueId {
    public Object value;
    public Object newValue;

    public Object getValue() {
        if(value != null) return value;
        if(newValue != null) return newValue;
        return null;
    }
}
