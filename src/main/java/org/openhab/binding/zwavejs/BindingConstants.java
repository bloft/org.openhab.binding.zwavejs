package org.openhab.binding.zwavejs;

import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.type.ChannelTypeUID;

public class BindingConstants {
    public static final String BINDING_ID = "zwavejs";

    public static final ThingTypeUID BRIDGE_TYPE = new ThingTypeUID(BINDING_ID, "bridge");
    public static final ThingTypeUID NODE_TYPE = new ThingTypeUID(BINDING_ID, "node");

    public static final ChannelTypeUID CHANNEL_STRING = new ChannelTypeUID(BINDING_ID, "string");
    public static final ChannelTypeUID CHANNEL_NUMBER = new ChannelTypeUID(BINDING_ID, "number");
    public static final ChannelTypeUID CHANNEL_CONTACT = new ChannelTypeUID(BINDING_ID, "contact");
    public static final ChannelTypeUID CHANNEL_SWITCH = new ChannelTypeUID(BINDING_ID, "switch");
    public static final ChannelTypeUID CHANNEL_COLOR = new ChannelTypeUID(BINDING_ID, "color");

    public static final ChannelTypeUID CHANNEL_BINARY_SWITCH = new ChannelTypeUID(BINDING_ID, "binary-switch");

    public static final ChannelTypeUID CHANNEL_TEMPERATURE = new ChannelTypeUID(BINDING_ID, "temperature");
    public static final ChannelTypeUID CHANNEL_HUMIDITY = new ChannelTypeUID(BINDING_ID, "humidity");
    public static final ChannelTypeUID CHANNEL_ILLUMINANCE = new ChannelTypeUID(BINDING_ID, "illuminance");
    public static final ChannelTypeUID CHANNEL_ULTRAVIOLET = new ChannelTypeUID(BINDING_ID, "ultraviolet");
    public static final ChannelTypeUID CHANNEL_PRESSURE = new ChannelTypeUID(BINDING_ID, "pressure");

    public static final ChannelTypeUID CHANNEL_METER = new ChannelTypeUID(BINDING_ID, "meter");

    public static final ChannelTypeUID CHANNEL_NOTIFICATION = new ChannelTypeUID(BINDING_ID, "notification");

    public static final ChannelTypeUID CHANNEL_BATTERY_LOW = new ChannelTypeUID(BINDING_ID, "low-battery");
    public static final ChannelTypeUID CHANNEL_BATTERY_LEVEL = new ChannelTypeUID(BINDING_ID, "battery-level");

    public static final String PARAM_NODE_ID = "nodeId";
}
