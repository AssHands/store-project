package com.ak.store.orderSagaWorker.mapper;

import com.ak.store.orderSagaWorker.model.entity.OutboxEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxEventMapper {
    private final Gson gson;

    public <T> OutboxEvent toOutboxEvent(T payload) {
        return OutboxEvent.builder()
                .payload(gson.toJson(payload))
                .build();
    }
}
