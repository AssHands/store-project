package com.ak.store.userOutbox.processor.impl;

import com.ak.store.common.kafka.user.VerifyUserEvent;
import com.ak.store.userOutbox.kafka.EventProducerKafka;
import com.ak.store.userOutbox.model.OutboxEvent;
import com.ak.store.userOutbox.model.OutboxEventType;
import com.ak.store.userOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerifyUserOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var verifyUserEvent = gson.fromJson(event.getPayload(), VerifyUserEvent.class);
        eventProducerKafka.send(verifyUserEvent, getType(), verifyUserEvent.getVerifyUser().getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.VERIFY_USER;
    }
}