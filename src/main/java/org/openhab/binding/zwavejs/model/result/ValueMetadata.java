package org.openhab.binding.zwavejs.model.result;

import java.util.Map;

public class ValueMetadata {
    private String type;
    private boolean readable;
    private boolean writeable;
    private String label;
    private String unit;
    private boolean stateful;
    private boolean secret;
    private Map<String, Object> states;

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
}
