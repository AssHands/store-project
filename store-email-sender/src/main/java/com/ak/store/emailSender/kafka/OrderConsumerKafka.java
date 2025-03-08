package com.ak.store.emailSender.kafka;

import com.ak.store.common.event.consumer.ConsumerVerifyEvent;
import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.emailSender.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final EmailSender emailSender;

    @KafkaListener(topics = "order-created-events", groupId = "email-sender-group")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        CompletableFuture.runAsync(() -> emailSender.sendOrderCreatedNotification(orderCreatedEvent));
    }
}
