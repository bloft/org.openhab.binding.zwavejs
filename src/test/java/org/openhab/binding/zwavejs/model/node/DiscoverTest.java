package org.openhab.binding.zwavejs.model.node;

import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.BindingConstants;
import org.openhab.binding.zwavejs.channel.ChannelDiscovery;
import org.openhab.binding.zwavejs.config.ChannelConfig;
import org.openhab.binding.zwavejs.model.AbstractModelTest;
import org.openhab.binding.zwavejs.model.result.DeviceStatus;
import org.openhab.binding.zwavejs.model.result.NodeState;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ThingUID;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class DiscoverTest extends AbstractModelTest {
    private void printChannels(NodeState state) {
        ThingUID thing = new ThingUID(BindingConstants.NODE_TYPE, "test");

        System.out.format("-={ %s - %s }=-\n", state.getNodeId(), state.getDeviceConfig().getDescription());

        List<Channel> channels = ChannelDiscovery.discover(thing, state.getValues());
        channels.forEach(channel -> {
            System.out.format("%s - %s (%s) - %s\n", channel.getConfiguration().as(ChannelConfig.class).asValueId().getId(), channel.getChannelTypeUID(), channel.getAcceptedItemType(), channel.getLabel());
        });
    }

    @Test
    public void discoverNode5() {
        NodeState state = fromClasspath("node5.json", NodeState.class);
        assertThat(state.getNodeId(), is(5));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.DEAD));

        printChannels(state);
    }

    @Test
    public void discoverNode6() {
        NodeState state = fromClasspath("node6.json", NodeState.class);
        assertThat(state.getNodeId(), is(6));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.ALIVE));

        printChannels(state);
    }

    @Test
    public void discoverNode7() {
        NodeState state = fromClasspath("node7.json", NodeState.class);
        assertThat(state.getNodeId(), is(7));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.ALIVE));

        printChannels(state);
    }

    @Test
    public void discoverNode8() {
        NodeState state = fromClasspath("node8.json", NodeState.class);
        assertThat(state.getNodeId(), is(8));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.ALIVE));

        printChannels(state);
    }

    @Test
    public void discoverNode9() {
        NodeState state = fromClasspath("node9.json", NodeState.class);
        assertThat(state.getNodeId(), is(9));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.ALIVE));

        printChannels(state);
    }

    @Test
    public void discoverNode10() {
        NodeState state = fromClasspath("node10.json", NodeState.class);
        assertThat(state.getNodeId(), is(10));
        assertThat(state.isReady(), is(true));
        assertThat(state.getStatus(), is(DeviceStatus.ALIVE));

        printChannels(state);
    }
}
