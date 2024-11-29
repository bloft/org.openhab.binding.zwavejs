package org.openhab.binding.zwavejs.channel;

import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.model.result.ValueMetadata;
import org.openhab.core.storage.StorageService;
import org.openhab.core.thing.binding.AbstractStorageBasedTypeProvider;
import org.openhab.core.thing.binding.ThingTypeProvider;
import org.openhab.core.thing.type.*;
import org.openhab.core.types.StateDescriptionFragment;
import org.openhab.core.types.StateDescriptionFragmentBuilder;
import org.openhab.core.types.StateOption;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.math.BigDecimal;
import java.util.Map;

// https://github.com/openhab/openhab-addons/blob/main/bundles/org.openhab.binding.mqtt.generic/src/main/java/org/openhab/binding/mqtt/generic/MqttChannelTypeProvider.java
@Component(service = {ThingTypeProvider.class, ChannelTypeProvider.class, ZWaveJSChannelTypeProvider.class})
public class ZWaveJSChannelTypeProvider extends AbstractStorageBasedTypeProvider {
    private final ThingTypeRegistry thingTypeRegistry;
    private final ChannelTypeRegistry channelTypeRegistry;

    private static final Map<String, String> TYPES = Map.of(
            "number", "Number",
            "string", "String",
            "boolean", "Switch"
    );
    private static final Map<String, String> DIMENSIONS = Map.of(
            "%", "Dimensionless",
            "kWh", "Energy",
            "W", "Power"
    );

    @Activate
    public ZWaveJSChannelTypeProvider(@Reference ChannelTypeRegistry channelTypeRegistry, @Reference ThingTypeRegistry thingTypeRegistry, @Reference StorageService storageService) {
        super(storageService);
        this.thingTypeRegistry = thingTypeRegistry;
        this.channelTypeRegistry = channelTypeRegistry;
    }

    protected static StateDescriptionFragment createStateDescription(ValueMetadata metadata) {
        StateDescriptionFragmentBuilder builder = StateDescriptionFragmentBuilder.create();
        if(metadata.getMin() != null) {
            builder.withMinimum(BigDecimal.valueOf(metadata.getMin()));
        }
        if(metadata.getMax() != null) {
            builder.withMaximum(BigDecimal.valueOf(metadata.getMax()));
        }
        if(metadata.getStates() != null) {
            metadata.getStates()
                    .entrySet()
                    .stream()
                    .map(entry -> new StateOption(entry.getKey(), entry.getValue().toString()))
                    .forEach(builder::withOption);
        }
        if(metadata.getUnit() != null) {
            builder.withPattern("%.1f " + metadata.getUnit());
        }
        builder.withReadOnly(!metadata.isWriteable());
        return builder.build();
    }

    protected static String getItemType(ValueMetadata metadata) {
        String type = TYPES.getOrDefault(metadata.getType(), "String");
        if(metadata.getUnit() != null && DIMENSIONS.containsKey(metadata.getUnit())) {
            type += ":" + DIMENSIONS.get(metadata.getUnit());
        }
        return type;
    }

    protected static ChannelType createChannelType(ValueMetadata metadata) {
        StateChannelTypeBuilder builder = ChannelTypeBuilder.state(new ChannelTypeUID(BindingConstants.BINDING_ID, ""), metadata.getLabel(), getItemType(metadata));
        builder.withStateDescriptionFragment(createStateDescription(metadata));
        if(metadata.getUnit() != null) {
            builder.withUnitHint(metadata.getUnit());
        }
        return builder.build();
    }

    /*
    public ChannelTypeUID createChannelType(ValueId valueId, String label) {
        new ChannelTypeUID(BindingConstants.BINDING_ID, valueId.getId());
        ChannelType channelType = ChannelTypeBuilder.state(null, label, "Number")
                .withStateDescriptionFragment(createStateDescription(valueId.getMetadata()))
                .build();
        putChannelType(channelType);
        return channelType.getUID();
    }
     */
}
