package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.snapshot.OrderSnapshot;
import com.ak.store.emailSender.errorHandler.OrderKafkaErrorHandler;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.inbox.InboxEventService;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.mapper.EmailMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final OrderKafkaErrorHandler errorHandler;
    private final InboxEventService<OrderSnapshot> inboxEventService;

    @Transactional
    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<OrderCreatedEvent> orderCreatedEvents) {
        for (var event : orderCreatedEvents) {
            try {
                inboxEventService.createOne(event.getEventId(), event.getOrder(), InboxEventType.ORDER_CREATED);
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }
}