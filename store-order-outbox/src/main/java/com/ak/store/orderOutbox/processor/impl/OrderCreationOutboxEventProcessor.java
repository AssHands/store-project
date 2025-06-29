package com.ak.store.orderOutbox.processor.impl;

import com.ak.store.common.kafka.order.OrderCreatedEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.order.OrderCreatedSnapshot;
import com.ak.store.orderOutbox.kafka.EventProducerKafka;
import com.ak.store.orderOutbox.mapper.JsonMapper;
import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;
    private final JsonMapper jsonMapper;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var orderCreatedEvent = new OrderCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), OrderCreatedSnapshot.class));

        var request = SagaRequestEvent.builder()
                .sagaId(UUID.randomUUID())
                .request(jsonMapper.toJsonNode(orderCreatedEvent.getOrder()))
                .build();

        String orderId = orderCreatedEvent.getOrder().getOrderId().toString();
        eventProducerKafka.send(request, getType(), orderId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.ORDER_CREATION;
    }
}
