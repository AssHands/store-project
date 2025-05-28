package com.ak.store.orderOutbox.processor.impl;

import com.ak.store.orderOutbox.kafka.EventProducerKafka;
import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.snapshot.OrderCreatedSnapshotPayload;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var orderCreatedEvent = new OrderCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), OrderCreatedSnapshotPayload.class));

        String orderId = orderCreatedEvent.getPayload().getOrder().getId().toString();
        eventProducerKafka.send(orderCreatedEvent, orderId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.ORDER_CREATED;
    }
}
