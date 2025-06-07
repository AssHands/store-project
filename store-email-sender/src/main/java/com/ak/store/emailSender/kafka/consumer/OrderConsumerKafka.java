package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.emailSender.errorHandler.OrderKafkaErrorHandler;
import com.ak.store.emailSender.service.InboxEventWriterService;
import com.ak.store.emailSender.inbox.InboxEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final InboxEventWriterService inboxEventWriterService;
    private final OrderKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<OrderCreatedEvent> orderCreatedEvents, Acknowledgment ack) {
        for (OrderCreatedEvent event : orderCreatedEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getPayload(), InboxEventType.ORDER_CREATED);
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }

        ack.acknowledge();
    }
}