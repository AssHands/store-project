package com.ak.store.sagaOrchestrator.kafka.consumer;

import com.ak.store.common.saga.SagaStatus;
import com.ak.store.common.saga.orderCreation.event.order.OrderCreationRequestEvent;
import com.ak.store.common.saga.orderCreation.event.order.OrderCreationResponseEvent;
import com.ak.store.sagaOrchestrator.facade.SagaFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderCreationConsumerKafka {
    private final SagaFacade sagaFacade;

    @Value("${saga.definitions.order-creation.name}")
    private String SAGA_ORDER_CREATION_NAME;

    @KafkaListener(topics = "${saga.definitions.order-creation.request-topic}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleRequest(List<OrderCreationRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            sagaFacade.createOne(event.getSagaId(), SAGA_ORDER_CREATION_NAME, event.getRequest());
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = {
            "${saga.definitions.order-creation.steps.reserve-funds.topics.response}",
            "${saga.definitions.order-creation.steps.reserve-products.topics.response}"},
            groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleResponse(List<OrderCreationResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            if (event.getStatus() == SagaStatus.SUCCESS) {
                sagaFacade.continueOne(event.getSagaId(), event.getStepName());
            } else {
                sagaFacade.compensateOne(event.getSagaId(), event.getStepName());
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = {
            "${saga.definitions.order-creation.steps.reserve-funds.topics.compensation-response}",
            "${saga.definitions.order-creation.steps.reserve-products.topics.compensation-response}"},
            groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCompensation(List<OrderCreationResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            sagaFacade.compensateOne(event.getSagaId(), event.getStepName());
        }

        ack.acknowledge();
    }
}