package com.ak.store.warehouseOutbox.processor.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.warehouseOutbox.kafka.EventProducerKafka;
import com.ak.store.warehouseOutbox.model.OutboxEvent;
import com.ak.store.warehouseOutbox.model.OutboxEventType;
import com.ak.store.warehouseOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseProductsOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var message = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(message, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.RELEASE_PRODUCTS;
    }
}