package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.snapshot.OrderSnapshot;
import com.ak.store.emailSender.errorHandler.OrderKafkaErrorHandler;
import com.ak.store.emailSender.service.InboxEventWriterService;
import com.ak.store.emailSender.inbox.InboxEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final OrderKafkaErrorHandler errorHandler;
    private final InboxEventWriterService<OrderSnapshot> inboxEventWriterService;

    @Transactional
    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<OrderCreatedEvent> orderCreatedEvents) {
        for (var event : orderCreatedEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getOrder(), InboxEventType.ORDER_CREATED);
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }
}