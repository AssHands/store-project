package com.ak.store.sagaOrchestrator.kafka.consumer;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;

import java.util.List;

public interface SagaConsumerKafka {
    void handle(List<Message<?>> messages, Acknowledgment ack);
}
