package org.openhab.binding.zwavejs.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

import java.util.HashSet;
import java.util.Set;

@NonNullByDefault
@Component(service = { ThingHandlerFactory.class }, configurationPid = "binding.zwavejs")
public class HandlerFactory extends BaseThingHandlerFactory {

    private final ChannelTypeRegistry channelTypeRegistry;

    @Activate
    public HandlerFactory(final @Reference ChannelTypeRegistry channelTypeRegistry) {
        this.channelTypeRegistry = channelTypeRegistry;
    }

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<>();
    static {
        SUPPORTED_THING_TYPES_UIDS.add(BindingConstants.BRIDGE_TYPE);
        SUPPORTED_THING_TYPES_UIDS.add(BindingConstants.NODE_TYPE);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if(thingTypeUID.equals(BindingConstants.BRIDGE_TYPE)) {
            return new BridgeHandler((Bridge) thing);
        } else if(thingTypeUID.equals(BindingConstants.NODE_TYPE)) {
            return new NodeHandler(thing, channelTypeRegistry);
        }
        return null;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }
}
