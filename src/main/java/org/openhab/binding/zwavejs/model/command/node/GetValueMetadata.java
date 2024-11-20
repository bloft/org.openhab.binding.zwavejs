package org.openhab.binding.zwavejs.model.command.node;

import org.openhab.binding.zwavejs.model.ValueId;

public class GetValueMetadata extends ValueCommand {
    public GetValueMetadata(int nodeId, ValueId valueId) {
        super(nodeId, valueId);
    }
}
