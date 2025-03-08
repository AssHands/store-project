package com.ak.store.order.kafka;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.event.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducerKafka {
    private final KafkaTemplate<String, OrderEvent> kafkaProductTemplate;

    public void send(OrderCreatedEvent orderCreatedEvent) {
        try {
            SendResult<String, OrderEvent> future = kafkaProductTemplate
                    .send("order-created-events", orderCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka product-deleted-events error");
        }
    }
}
