package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.emailSender.errorHandler.OrderKafkaErrorHandler;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final EmailFacade emailFacade;
    private final EmailMapper emailMapper;
    private final OrderKafkaErrorHandler errorHandler;

    @Qualifier("emailExecutor")
    private ExecutorService emailExecutor;

    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${kafka.group-id}", batch = "true")
    public void handle(List<OrderCreatedEvent> orderCreatedEvents) {
        for (var event : orderCreatedEvents) {
            try {
                emailFacade.sendOrderCreated(
                        emailMapper.toOrderCreatedWriteDTO(event));
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }
}