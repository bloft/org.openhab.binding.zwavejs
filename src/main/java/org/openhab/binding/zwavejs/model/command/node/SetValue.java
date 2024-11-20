package org.openhab.binding.zwavejs.model.command.node;

import org.openhab.binding.zwavejs.model.ValueId;

public class SetValue extends ValueCommand {
    private final Object value;

    public SetValue(int nodeId, ValueId valueId, Object value) {
        super(nodeId, valueId);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
