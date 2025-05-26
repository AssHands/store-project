package com.ak.store.catalogue.outbox;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxEventMapper<T> {
    private final Gson gson;

    public OutboxEvent mapToOutboxTask(T payload) {
        return OutboxEvent.builder()
                .payload(gson.toJson(payload))
                .build();
    }
}
