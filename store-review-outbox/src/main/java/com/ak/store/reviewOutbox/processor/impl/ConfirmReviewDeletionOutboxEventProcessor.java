package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.reviewOutbox.kafka.EventProducerKafka;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmReviewDeletionOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var response = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(response, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CONFIRM_REVIEW_DELETION;
    }
}
