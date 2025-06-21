package com.ak.store.sagaOrchestrator.mapper;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SagaMapper {
    private final Gson gson;

    public <T> String toPayload(T payload) {
        return gson.toJson(payload);
    }
}
