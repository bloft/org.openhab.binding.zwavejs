package org.openhab.binding.zwavejs.model.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openhab.binding.zwavejs.model.Message;

import java.util.Map;

public class Result extends Message {
    private boolean success;
    private String messageId;

    private Map<String, Object> result;

    private String errorCode;

    private Integer zwaveErrorCode;
    private String zwaveErrorMessage;

    public boolean isSuccess() {
        return success;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public <T> T getResult(Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String res = mapper.writeValueAsString(result);
            return mapper.readValue(res, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Integer getZwaveErrorCode() {
        return zwaveErrorCode;
    }

    public String getZwaveErrorMessage() {
        return zwaveErrorMessage;
    }
}
