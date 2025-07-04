package com.ak.store.userOutbox.processor.impl;

import com.ak.store.common.kafka.user.UserCreatedEvent;
import com.ak.store.userOutbox.kafka.EventProducerKafka;
import com.ak.store.userOutbox.model.OutboxEvent;
import com.ak.store.userOutbox.model.OutboxEventType;
import com.ak.store.userOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var userCreatedEvent = gson.fromJson(event.getPayload(), UserCreatedEvent.class);
        eventProducerKafka.send(userCreatedEvent, getType(), userCreatedEvent.getVerifyUser().getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.USER_CREATED;
    }
}
