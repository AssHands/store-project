package com.ak.store.emailSender.inbox;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InboxEventMapper {
    private final Gson gson;

    public <T> InboxEvent toInboxEvent(T payload) {
        return InboxEvent.builder()
                .payload(gson.toJson(payload))
                .build();
    }
}
