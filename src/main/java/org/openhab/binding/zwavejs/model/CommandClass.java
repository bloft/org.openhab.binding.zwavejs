package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CommandClass {
    ALARM_SENSOR("Alarm Sensor", 0x9c),
    ALARM_SILENCE("Alarm Silence", 0x9d),
    ALL_SWITCH("All Switch", 0x27),
    ANTI_THEFT("Anti-Theft", 0x5d),
    ANTI_THEFT_UNLOCK("Anti-Theft Unlock", 0x7e),
    APPLICATION_CAPABILITY("Application Capability", 0x57),
    APPLICATION_STATUS("Application Status", 0x22),
    ASSOCIATION("Association", 0x85),
    ASSOCIATION_COMMAND_CONFIGURATION("Association Command Configuration", 0x9b),
    ASSOCIATION_GROUP_INFORMATION("Association Group Information", 0x59),
    AUTHENTICATION("Authentication", 0xa1),
    AUTHENTICATION_MEDIA_WRITE("Authentication Media Write", 0xa2),
    BARRIER_OPERATOR("Barrier Operator", 0x66),
    BASIC("Basic", 0x20),
    BASIC_TARIFF_INFORMATION("Basic Tariff Information", 0x36),
    BASIC_WINDOW_COVERING("Basic Window Covering", 0x50),
    BATTERY("Battery", 0x80),
    BINARY_SENSOR("Binary Sensor", 0x30),
    BINARY_SWITCH("Binary Switch", 0x25),
    BINARY_TOGGLE_SWITCH("Binary Toggle Switch", 0x28),
    CENTRAL_SCENE("Central Scene", 0x5b),
    CLIMATE_CONTROL_SCHEDULE("Climate Control Schedule", 0x46),
    CLOCK("Clock", 0x81),
    COLOR_SWITCH("Color Switch", 0x33),
    CONFIGURATION("Configuration", 0x70),
    CONTROLLER_REPLICATION("Controller Replication", 0x21),
    CRC16_ENCAPSULATION("CRC-16 Encapsulation", 0x56),
    DEMAND_CONTROL_PLAN_CONFIGURATION("Demand Control Plan Configuration", 0x3a),
    DEMAND_CONTROL_PLAN_MONITOR("Demand Control Plan Monitor", 0x3b),
    DEVICE_RESET_LOCALLY("Device Reset Locally", 0x5a),
    DOOR_LOCK("Door Lock", 0x62),
    DOOR_LOCK_LOGGING("Door Lock Logging", 0x4c),
    ENERGY_PRODUCTION("Energy Production", 0x90),
    ENTRY_CONTROL("Entry Control", 0x6f),
    FIRMWARE_UPDATE_META_DATA("Firmware Update Meta Data", 0x7a),
    GENERIC_SCHEDULE("Generic Schedule", 0xa3),
    GEOGRAPHIC_LOCATION("Geographic Location", 0x8c),
    GROUPING_NAME("Grouping Name", 0x7b),
    HAIL("Hail", 0x82),
    HRV_CONTROL("HRV Control", 0x39),
    HRV_STATUS("HRV Status", 0x37),
    HUMIDITY_CONTROL_MODE("Humidity Control Mode", 0x6d),
    HUMIDITY_CONTROL_OPERATING_STATE("Humidity Control Operating State", 0x6e),
    HUMIDITY_CONTROL_SETPOINT("Humidity Control Setpoint", 0x64),
    INCLUSION_CONTROLLER("Inclusion Controller", 0x74),
    INDICATOR("Indicator", 0x87),
    IP_ASSOCIATION("IP Association", 0x5c),
    IP_CONFIGURATION("IP Configuration", 0x9a),
    IR_REPEATER("IR_Repeater", 0xa0),
    IRRIGATION("Irrigation", 0x6b),
    LANGUAGE("Language", 0x89),
    LOCK("Lock", 0x76),
    MAILBOX("Mailbox", 0x69),
    MANUFACTURER_PROPRIETARY("Manufacturer Proprietary", 0x91),
    MANUFACTURER_SPECIFIC("Manufacturer Specific", 0x72),
    METER("Meter", 0x32),
    METER_TABLE_CONFIGURATION("Meter Table Configuration", 0x3c),
    METER_TABLE_MONITOR("Meter Table Monitor", 0x3d),
    METER_TABLE_PUSH_CONFIGURATION("Meter Table Push Configuration", 0x3e),
    MOVE_TO_POSITION_WINDOW_COVERING("Move To Position Window Covering", 0x51),
    MULTI_CHANNEL("Multi Channel", 0x60),
    MULTI_CHANNEL_ASSOCIATION("Multi Channel Association", 0x8e),
    MULTI_COMMAND("Multi Command", 0x8f),
    MULTILEVEL_SENSOR("Multilevel Sensor", 0x31),
    MULTILEVEL_SWITCH("Multilevel Switch", 0x26),
    MULTILEVEL_TOGGLE_SWITCH("Multilevel Toggle Switch", 0x29),
    NETWORK_MANAGEMENT_BASIC_NODE("Network Management Basic Node", 0x4d),
    NETWORK_MANAGEMENT_INCLUSION("Network Management Inclusion", 0x34),
    NETWORK_MANAGEMENT_INSTALLATION_AND_MAINTENANCE("Network Management Installation and Maintenance", 0x67),
    NETWORK_MANAGEMENT_PRIMARY("Network Management Primary", 0x54),
    NETWORK_MANAGEMENT_PROXY("Network Management Proxy", 0x52),
    NO_OPERATION("No Operation", 0x00),
    NODE_NAMING_AND_LOCATION("Node Naming and Location", 0x77),
    NODE_PROVISIONING("Node Provisioning", 0x78),
    NOTIFICATION("Notification", 0x71),
    POWERLEVEL("Powerlevel", 0x73),
    PREPAYMENT("Prepayment", 0x3f),
    PREPAYMENT_ENCAPSULATION("Prepayment Encapsulation", 0x41),
    PROPRIETARY("Proprietary", 0x88),
    PROTECTION("Protection", 0x75),
    PULSE_METER("Pulse Meter", 0x35),
    RATE_TABLE_CONFIGURATION("Rate Table Configuration", 0x48),
    RATE_TABLE_MONITOR("Rate Table Monitor", 0x49),
    REMOTE_ASSOCIATION_ACTIVATION("Remote Association Activation", 0x7c),
    REMOTE_ASSOCIATION_CONFIGURATION("Remote Association Configuration", 0x7d),
    SCENE_ACTIVATION("Scene Activation", 0x2b),
    SCENE_ACTUATOR_CONFIGURATION("Scene Actuator Configuration", 0x2c),
    SCENE_CONTROLLER_CONFIGURATION("Scene Controller Configuration", 0x2d),
    SCHEDULE("Schedule", 0x53),
    SCHEDULE_ENTRY_LOCK("Schedule Entry Lock", 0x4e),
    SCREEN_ATTRIBUTES("Screen Attributes", 0x93),
    SCREEN_META_DATA("Screen Meta Data", 0x92),
    SECURITY("Security", 0x98),
    SECURITY_2("Security 2", 0x9f),
    SECURITY_MARK("Security Mark", 0xf100),
    SENSOR_CONFIGURATION("Sensor Configuration", 0x9e),
    SIMPLE_AV_CONTROL("Simple AV Control", 0x94),
    SOUND_SWITCH("Sound Switch", 0x79),
    SUPERVISION("Supervision", 0x6c),
    SUPPORT_CONTROL_MARK("Support/Control Mark", 0xef),
    TARIFF_TABLE_CONFIGURATION("Tariff Table Configuration", 0x4a),
    TARIFF_TABLE_MONITOR("Tariff Table Monitor", 0x4b),
    THERMOSTAT_FAN_MODE("Thermostat Fan Mode", 0x44),
    THERMOSTAT_FAN_STATE("Thermostat Fan State", 0x45),
    THERMOSTAT_MODE("Thermostat Mode", 0x40),
    THERMOSTAT_OPERATING_STATE("Thermostat Operating State", 0x42),
    THERMOSTAT_SETBACK("Thermostat Setback", 0x47),
    THERMOSTAT_SETPOINT("Thermostat Setpoint", 0x43),
    TIME("Time", 0x8a),
    TIME_PARAMETERS("Time Parameters", 0x8b),
    TRANSPORT_SERVICE("Transport Service", 0x55),
    USER_CODE("User Code", 0x63),
    USER_CREDENTIAL("User Credential", 0x83),
    VERSION("Version", 0x86),
    WAKE_UP("Wake Up", 0x84),
    WINDOW_COVERING("Window Covering", 0x6a),
    Z_IP("Z/IP", 0x23),
    Z_IP_6LOWPAN("Z/IP 6LoWPAN", 0x4f),
    Z_IP_GATEWAY("Z/IP Gateway", 0x5f),
    Z_IP_NAMING_AND_LOCATION("Z/IP Naming and Location", 0x68),
    Z_IP_ND("Z/IP ND", 0x58),
    Z_IP_PORTAL("Z/IP Portal", 0x61),
    Z_WAVE_PLUS_INFO("Z-Wave Plus Info", 0x5e),
    Z_WAVE_PROTOCOL("Z-Wave Protocol", 0x01),
    Z_WAVE_LONG_RANGE("Z-Wave Long Range", 0x04);

    private final String name;

    @JsonValue
    private final int id;

    CommandClass(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static final CommandClass byId(int id) {
        return Arrays.stream(CommandClass.values()).filter(c -> c.getId() == id).findAny().orElseThrow();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
