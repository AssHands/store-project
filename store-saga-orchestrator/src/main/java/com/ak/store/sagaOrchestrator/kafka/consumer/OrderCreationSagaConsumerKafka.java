package com.ak.store.sagaOrchestrator.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component("order-creation")
public class OrderCreationSagaConsumerKafka implements SagaConsumerKafka {

    @Override
    public void handle(List<Message<?>> messages, Acknowledgment ack) {

    }
}