package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.saga.SagaResponseEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelDeleteProductGradeOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var response = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(response, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CANCEL_DELETE_PRODUCT_GRADE;
    }
}
