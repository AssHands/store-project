package com.ak.sotre.orderOutbox.processor.impl;

import com.ak.sotre.orderOutbox.kafka.EventProducerKafka;
import com.ak.sotre.orderOutbox.model.OutboxEvent;
import com.ak.sotre.orderOutbox.model.OutboxEventType;
import com.ak.sotre.orderOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.snapshot.OrderSnapshot;
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
                gson.fromJson(event.getPayload(), OrderSnapshot.class));

        String orderId = orderCreatedEvent.getOrder().getId().toString();
        eventProducerKafka.send(orderCreatedEvent, orderId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.ORDER_CREATED;
    }
}
