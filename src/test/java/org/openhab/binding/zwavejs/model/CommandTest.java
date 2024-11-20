package org.openhab.binding.zwavejs.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.openhab.binding.zwavejs.model.command.SetApiSchema;
import org.openhab.binding.zwavejs.model.command.StartListening;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class CommandTest extends AbstractModelTest {
    @Test
    void testSetApiSchema() throws JsonProcessingException {
        Command cmd = new SetApiSchema(10);
        cmd.setMessageId("1");
        String result = getMapper().writeValueAsString(cmd);
        assertThat(result, containsString("set_api_schema"));
        assertThat(result, containsString("\"messageId\":\"1\""));
    }

    @Test
    void testStartListening() throws JsonProcessingException {
        Command cmd = new StartListening();
        cmd.setMessageId("1");
        String result = getMapper().writeValueAsString(cmd);
        assertThat(result, containsString("start_listening"));
        assertThat(result, containsString("\"messageId\":\"1\""));
    }
}