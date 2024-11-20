package org.openhab.binding.zwavejs.config;

import java.net.URI;
import java.net.URISyntaxException;

public class BridgeConfig {
    public String host;
    public int port;

    public URI getURI() throws URISyntaxException {
        return new URI("ws://" + host + ":" + port);
    }
}
