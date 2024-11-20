package org.openhab.binding.zwavejs.discovery;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.ThingUID;

import java.util.Collections;

public class ThingDiscoveryService extends AbstractDiscoveryService {
    public static ThingDiscoveryService instance;

    public ThingDiscoveryService() {
        super(Collections.singleton(BindingConstants.NODE_TYPE), 10, true);
        instance = this;
    }

    public static ThingDiscoveryService getInstance() {
        return instance;
    }

    @Override
    protected void startScan() {

    }

    public ThingUID getUID(ThingUID bridgeUID, int nodeId) {
        return new ThingUID(BindingConstants.NODE_TYPE, bridgeUID, Integer.toString(nodeId));
    }

    public void newNode(ThingUID bridgeUID, int nodeId, String label) {
        if(isBackgroundDiscoveryEnabled()) {
            DiscoveryResult discoveryResult = DiscoveryResultBuilder
                    .create(getUID(bridgeUID, nodeId))
                    .withProperty(BindingConstants.PARAM_NODE_ID, nodeId)
                    .withBridge(bridgeUID)
                    .withLabel(label)
                    .build();
            thingDiscovered(discoveryResult);
        }
    }
}
