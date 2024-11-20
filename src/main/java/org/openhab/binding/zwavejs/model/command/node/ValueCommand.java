package org.openhab.binding.zwavejs.model.command.node;

import org.openhab.binding.zwavejs.model.ValueId;

public abstract class ValueCommand extends NodeCommand {
    private final ValueId valueId;

    public ValueCommand(int nodeId, ValueId valueId) {
        super(nodeId);
        this.valueId = valueId;
    }

    public ValueId getValueId() {
        return valueId;
    }
}
