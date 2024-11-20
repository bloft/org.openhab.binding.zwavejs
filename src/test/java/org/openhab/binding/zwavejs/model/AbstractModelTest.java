package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AbstractModelTest {
    private final ObjectMapper mapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    protected <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), clazz);
    }

    protected <T> T fromJson(InputStream json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T fromClasspath(String filename, Class<T> clazz) {
        String path = String.format("/%s/%s", this.getClass().getPackageName().replace(".", "/"), filename);
        return fromJson(this.getClass().getResourceAsStream(path), clazz);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
