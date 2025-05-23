package com.ak.store.catalogue.outbox;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxTaskMapper<T> {
    private final Gson gson;

    public OutboxTask mapToOutboxTask(T payload) {
        return OutboxTask.builder()
                .payload(gson.toJson(payload))
                .build();
    }
}
