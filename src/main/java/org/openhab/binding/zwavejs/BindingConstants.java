package org.openhab.binding.zwavejs;

import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.type.ChannelTypeUID;

public class BindingConstants {
    private static final String BINDING_ID = "zwavejs";

    public static final ThingTypeUID BRIDGE_TYPE = new ThingTypeUID(BINDING_ID, "bridge");
    public static final ThingTypeUID NODE_TYPE = new ThingTypeUID(BINDING_ID, "node");

    public static final ChannelTypeUID CHANNEL_STRING = new ChannelTypeUID(BINDING_ID, "string");
    public static final ChannelTypeUID CHANNEL_NUMBER = new ChannelTypeUID(BINDING_ID, "number");
    public static final ChannelTypeUID CHANNEL_CONTACT = new ChannelTypeUID(BINDING_ID, "contact");
    public static final ChannelTypeUID CHANNEL_SWITCH = new ChannelTypeUID(BINDING_ID, "switch");

    public static final String PARAM_NODE_ID = "nodeId";
}
