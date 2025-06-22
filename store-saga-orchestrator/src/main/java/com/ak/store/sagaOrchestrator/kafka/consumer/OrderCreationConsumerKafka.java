package com.ak.store.sagaOrchestrator.kafka.consumer;

import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderCreationConsumerKafka {
    private final SagaService sagaService;
    private final SagaStepService sagaStepService;

    @Value("${saga.definitions.order-creation.name}")
    private String SAGA_ORDER_CREATION_NAME;

    @KafkaListener(topics = "${saga.definitions.order-creation.request-topic}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleRequest(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            sagaService.createOne(event.getSagaId(), SAGA_ORDER_CREATION_NAME, event.getRequest().toString());
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = {
            "${saga.definitions.order-creation.steps.reserve-funds.topics.response}",
            "${saga.definitions.order-creation.steps.reserve-products.topics.response}"},
            groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleResponse(List<SagaResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            if (event.getStatus() == SagaResponseStatus.SUCCESS) {
                sagaStepService.continueOne(event.getSagaId(), event.getStepName());
            } else {
                sagaStepService.compensateOne(event.getSagaId(), event.getStepName());
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = {
            "${saga.definitions.order-creation.steps.reserve-funds.topics.compensation-response}",
            "${saga.definitions.order-creation.steps.reserve-products.topics.compensation-response}"},
            groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCompensation(List<SagaResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            sagaStepService.compensateOne(event.getSagaId(), event.getStepName());
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${saga.definitions.order-creation.response-topic}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<SagaResponseEvent> events, Acknowledgment ack) {
        var ids = events.stream()
                .map(SagaResponseEvent::getSagaId)
                .toList();

        sagaService.markAllAsByIds(ids, SagaStatus.COMPLETED);
        ack.acknowledge();
    }
}