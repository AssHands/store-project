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

    @Value("${saga.definitions.order-creation.steps.reserve-funds.name}")
    private String RESERVE_FUNDS_STEP_NAME;

    @Value("${saga.definitions.order-creation.steps.reserve-products.name}")
    private String RESERVE_PRODUCTS_STEP_NAME;

    @KafkaListener(topics = "${saga.definitions.order-creation.request-topic}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleOrderCreationRequest(List<OrderCreationRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            sagaFacade.createOne(event.getSagaId(), event);
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-funds.topics.response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handlePaymentReserveFundsResponse(List<OrderCreationResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            if (event.getStatus() == SagaStatus.SUCCESS) {
                sagaFacade.continueOne(event.getSagaId(), RESERVE_FUNDS_STEP_NAME);
            } else {
                sagaFacade.compensateOne(event.getSagaId(), RESERVE_FUNDS_STEP_NAME);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-products.topics.response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleWarehouseReserveProductsResponse(List<OrderCreationResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            if (event.getStatus() == SagaStatus.SUCCESS) {
                sagaFacade.continueOne(event.getSagaId(), RESERVE_PRODUCTS_STEP_NAME);
            } else {
                sagaFacade.compensateOne(event.getSagaId(), RESERVE_PRODUCTS_STEP_NAME);
            }
        }

        ack.acknowledge();
    }

    //----- COMPENSATION -----

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-funds.topics.compensation-response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handlePaymentReleaseFundsResponse(List<OrderCreationResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            if (event.getStatus() == SagaStatus.SUCCESS) {
                sagaFacade.compensateOne(event.getSagaId(), RESERVE_FUNDS_STEP_NAME);
            } else {
                //todo что делать, если компенсирующий запрос провалился
            }
        }

        ack.acknowledge();
    }
}