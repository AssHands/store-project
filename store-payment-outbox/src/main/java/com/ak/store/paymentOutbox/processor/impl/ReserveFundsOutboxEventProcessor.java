package com.ak.store.paymentOutbox.processor.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.paymentOutbox.kafka.EventProducerKafka;
import com.ak.store.paymentOutbox.model.OutboxEvent;
import com.ak.store.paymentOutbox.model.OutboxEventType;
import com.ak.store.paymentOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveFundsOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var response = gson.fromJson(event.getPayload(), SagaResponseEvent.class);
        eventProducerKafka.send(response, getType(), event.getId().toString());
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.RESERVE_FUNDS;
    }
}