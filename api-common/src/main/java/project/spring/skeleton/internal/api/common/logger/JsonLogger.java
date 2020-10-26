package project.spring.skeleton.internal.api.common.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonLogger {
    public void log(String text) {
        try {
            log.info(new ObjectMapper().writeValueAsString(text));
        } catch (JsonProcessingException ex) {
            log.error("JsonLogger#log, json processing failed.", ex);
        }
    }

    public void log(JsonTemplate template) {
        try {
            log.info(new ObjectMapper().writeValueAsString(template));
        } catch (JsonProcessingException ex) {
            log.error("JsonLogger#log, json processing failed.", ex);
        }
    }
}