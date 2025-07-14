package com.ak.store.userOutbox.processor.impl;

import com.ak.store.common.kafka.user.UserVerificationEvent;
import com.ak.store.common.snapshot.user.UserVerificationSnapshot;
import com.ak.store.userOutbox.kafka.EventProducerKafka;
import com.ak.store.userOutbox.model.OutboxEvent;
import com.ak.store.userOutbox.model.OutboxEventType;
import com.ak.store.userOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserVerificationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var message = new UserVerificationEvent(event.getId(),
                gson.fromJson(event.getPayload(), UserVerificationSnapshot.class));

        eventProducerKafka.send(message, getType(), message.getRequest().getUserId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.USER_VERIFICATION;
    }
}
