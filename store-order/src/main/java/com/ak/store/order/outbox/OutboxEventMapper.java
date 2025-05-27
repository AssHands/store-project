package com.ak.store.order.outbox;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxEventMapper<T> {
    private final Gson gson;

    public OutboxEvent toOutboxEvent(T payload) {
        return OutboxEvent.builder()
                .payload(gson.toJson(payload))
                .build();
    }
}
