package com.ak.store.orderOutbox.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonMapper {
    private final ObjectMapper objectMapper;
    private final Gson gson;

    public JsonNode toJsonNode(String payload) throws JsonProcessingException {
        return objectMapper.readTree(payload);
    }

    public <T> JsonNode toJsonNode(T payload) throws JsonProcessingException {
        return objectMapper.readTree(gson.toJson(payload));
    }
}