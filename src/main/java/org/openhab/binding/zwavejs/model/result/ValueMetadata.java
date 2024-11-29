package org.openhab.binding.zwavejs.model.result;

import java.util.Map;

public class ValueMetadata {
    public String type;
    public boolean readable;
    public boolean writeable;
    public Integer min;
    public Integer max;
    public String label;
    public String unit;
    public boolean stateful;
    public boolean secret;
    public Map<String, Object> states;

    public String getType() {
        return type;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public String getLabel() {
        return label;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isStateful() {
        return stateful;
    }

    public boolean isSecret() {
        return secret;
    }

    public Map<String, Object> getStates() {
        return states;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
