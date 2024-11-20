package org.openhab.binding.zwavejs.model.command.node;

import org.openhab.binding.zwavejs.model.Command;

public abstract class NodeCommand extends Command {
    private final int nodeId;

    public NodeCommand(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
