package com.ak.store.userRegistrationOutbox.processor.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.userRegistrationOutbox.kafka.EventProducerKafka;
import com.ak.store.userRegistrationOutbox.model.OutboxEvent;
import com.ak.store.userRegistrationOutbox.model.OutboxEventType;
import com.ak.store.userRegistrationOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserRegistrationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var response = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(response, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CANCEL_USER_REGISTRATION;
    }
}
