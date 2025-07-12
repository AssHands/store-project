package com.ak.store.userRegistrationOutbox.processor.impl;

import com.ak.store.common.kafka.user.UserCreationEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.user.UserCreatedSnapshot;
import com.ak.store.userRegistrationOutbox.kafka.EventProducerKafka;
import com.ak.store.userRegistrationOutbox.mapper.JsonMapper;
import com.ak.store.userRegistrationOutbox.model.OutboxEvent;
import com.ak.store.userRegistrationOutbox.model.OutboxEventType;
import com.ak.store.userRegistrationOutbox.processor.OutboxEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRegistrationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;
    private final JsonMapper jsonMapper;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var userRegistrationEvent = new UserCreationEvent(event.getId(),
                gson.fromJson(event.getPayload(), UserCreatedSnapshot.class));

        var request = SagaRequestEvent.builder()
                .sagaId(event.getId())
                .request(jsonMapper.toJsonNode(userRegistrationEvent.getRequest()))
                .build();

        String userId = userRegistrationEvent.getRequest().getUserId().toString();
        eventProducerKafka.send(request, getType(), userId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.USER_REGISTRATION;
    }
}