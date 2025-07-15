package com.ak.store.SynchronizationOutbox.processor.impl;

import com.ak.store.SynchronizationOutbox.kafka.EventProducerKafka;
import com.ak.store.SynchronizationOutbox.model.OutboxEvent;
import com.ak.store.SynchronizationOutbox.model.OutboxEventType;
import com.ak.store.SynchronizationOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.saga.SagaResponseEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductSynchronizationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var message = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(message, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION;
    }
}
