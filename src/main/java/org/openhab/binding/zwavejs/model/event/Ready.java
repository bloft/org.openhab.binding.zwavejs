package org.openhab.binding.zwavejs.model.event;

import org.openhab.binding.zwavejs.model.Event;
import org.openhab.binding.zwavejs.model.result.NodeState;

public class Ready extends Event {
    private NodeState nodeState;

    public NodeState getNodeState() {
        return nodeState;
    }
}
