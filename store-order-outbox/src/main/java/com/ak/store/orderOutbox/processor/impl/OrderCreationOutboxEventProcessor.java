package com.ak.store.orderOutbox.processor.impl;

import com.ak.store.common.kafka.order.OrderCreationEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.order.OrderCreationSnapshot;
import com.ak.store.orderOutbox.kafka.EventProducerKafka;
import com.ak.store.orderOutbox.mapper.JsonMapper;
import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;
    private final JsonMapper jsonMapper;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var orderCreatedEvent = new OrderCreationEvent(event.getId(),
                gson.fromJson(event.getPayload(), OrderCreationSnapshot.class));

        var request = SagaRequestEvent.builder()
                .sagaId(event.getId())
                .request(jsonMapper.toJsonNode(orderCreatedEvent.getRequest()))
                .build();

        String orderId = orderCreatedEvent.getRequest().getOrderId().toString();
        eventProducerKafka.send(request, getType(), orderId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.ORDER_CREATION;
    }
}
