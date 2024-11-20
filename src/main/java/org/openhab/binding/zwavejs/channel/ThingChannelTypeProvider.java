package org.openhab.binding.zwavejs.channel;

import org.openhab.core.storage.StorageService;
import org.openhab.core.thing.binding.AbstractStorageBasedTypeProvider;

// https://github.com/openhab/openhab-addons/blob/main/bundles/org.openhab.binding.mqtt.generic/src/main/java/org/openhab/binding/mqtt/generic/MqttChannelTypeProvider.java
// @Component(service = {ChannelTypeProvider.class, ThingChannelTypeProvider.class})
public class ThingChannelTypeProvider extends AbstractStorageBasedTypeProvider {
    protected ThingChannelTypeProvider(StorageService storageService) {
        super(storageService);
    }
}
