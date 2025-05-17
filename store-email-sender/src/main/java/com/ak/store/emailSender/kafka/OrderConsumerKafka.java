package com.ak.store.emailSender.kafka;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderConsumerKafka {
    private final EmailFacade emailFacade;
    private final EmailMapper emailMapper;

    //todo add batches
    @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "${kafka.group-id}")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        CompletableFuture.runAsync(() -> emailFacade.sendOrderCreated(
                emailMapper.toOrderCreatedWriteDTO(orderCreatedEvent))
        );
    }
}
