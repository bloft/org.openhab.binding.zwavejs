package org.openhab.binding.zwavejs.model.command.node;

import org.openhab.binding.zwavejs.model.ValueId;

public class GetValue extends ValueCommand{
    public GetValue(int nodeId, ValueId valueId) {
        super(nodeId, valueId);
    }
}
