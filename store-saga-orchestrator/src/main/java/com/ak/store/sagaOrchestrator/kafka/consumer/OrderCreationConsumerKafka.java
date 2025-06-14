package com.ak.store.sagaOrchestrator.kafka.consumer;

import com.ak.store.common.saga.orderCreation.event.order.OrderCreationRequestEvent;
import com.ak.store.common.saga.orderCreation.event.order.OrderCreationResponseEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReleaseFundsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReleaseFundsResponseEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReserveFundsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReserveFundsResponseEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReserveProductsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReserveProductsResponseEvent;
import com.ak.store.sagaOrchestrator.kafka.producer.EventProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderCreationConsumerKafka {
    private final EventProducerKafka eventProducerKafka;

    @KafkaListener(topics = "${saga.definitions.order-creation.request-topic}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleOrderCreationRequest(List<OrderCreationRequestEvent> orderCreationRequestEvents, Acknowledgment ack) {
        for (var event : orderCreationRequestEvents) {
            eventProducerKafka.send(new PaymentReserveFundsRequestEvent(), event.getSagaId().toString());
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-funds.topics.response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handlePaymentReserveFundsResponse(List<PaymentReserveFundsResponseEvent> paymentReserveFundsResponseEvents,
                                                  Acknowledgment ack) {
        for (var event : paymentReserveFundsResponseEvents) {
            switch (event.getStatus()) {
                case SUCCESS ->
                        eventProducerKafka.send(new WarehouseReserveProductsRequestEvent(), event.getSagaId().toString());
                case FAILURE -> eventProducerKafka.send(new OrderCreationResponseEvent(), event.getSagaId().toString());
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-products.topics.response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleWarehouseReserveProductsResponse(List<WarehouseReserveProductsResponseEvent> warehouseReserveProductsResponseEvents,
                                                       Acknowledgment ack) {
        for (var event : warehouseReserveProductsResponseEvents) {
            switch (event.getStatus()) {
                case SUCCESS -> eventProducerKafka.send(new OrderCreationResponseEvent(), event.getSagaId().toString());
                case FAILURE ->
                        eventProducerKafka.send(new PaymentReleaseFundsRequestEvent(), event.getSagaId().toString());
            }
        }

        ack.acknowledge();
    }

    //----- COMPENSATION -----

    @KafkaListener(topics = "${saga.definitions.order-creation.steps.reserve-funds.topics.compensation-response}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handlePaymentReleaseFundsResponse(List<PaymentReleaseFundsResponseEvent> paymentReleaseFundsResponses,
                                                 Acknowledgment ack) {
        for (var event : paymentReleaseFundsResponses) {
            switch (event.getStatus()) {
                case SUCCESS -> eventProducerKafka.send(new OrderCreationResponseEvent(), event.getSagaId().toString());
                //todo что делать если компенсирующая транзакция провалилась
                //case FAILURE ->
            }
        }

        ack.acknowledge();
    }
}