package com.ak.store.sagaOrchestrator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonMapper {
    private final ObjectMapper objectMapper;

    public JsonNode toJsonNode(String payload) throws JsonProcessingException {
        return objectMapper.readTree(payload);
    }
}