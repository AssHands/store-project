package com.ak.store.sagaOrchestrator.util;

import com.ak.store.common.event.KafkaEvent;
import com.ak.store.common.saga.orderCreation.event.order.OrderCreationRequestEvent;
import com.ak.store.common.saga.orderCreation.event.order.OrderCreationResponseEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReleaseFundsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReleaseFundsResponseEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReserveFundsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.payment.PaymentReserveFundsResponseEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReleaseProductsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReleaseProductsResponseEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReserveProductsRequestEvent;
import com.ak.store.common.saga.orderCreation.event.warehouse.WarehouseReserveProductsResponseEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicRegistry {
    private final SagaProperties sagaProperties;
    private final Map<Class<? extends KafkaEvent>, String> eventTopicMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<Class<? extends KafkaEvent>, String> eventKeyMap = Map.ofEntries(
                Map.entry(OrderCreationRequestEvent.class, "order-creation-request"),
                Map.entry(OrderCreationResponseEvent.class, "order-creation-response"),

                Map.entry(PaymentReserveFundsRequestEvent.class, "payment-reserve-funds-request"),
                Map.entry(PaymentReserveFundsResponseEvent.class, "payment-reserve-funds-response"),
                Map.entry(PaymentReleaseFundsRequestEvent.class, "payment-release-funds-request"),
                Map.entry(PaymentReleaseFundsResponseEvent.class, "payment-release-funds-response"),

                Map.entry(WarehouseReserveProductsRequestEvent.class, "warehouse-reserve-products-request"),
                Map.entry(WarehouseReleaseProductsRequestEvent.class, "warehouse-release-products-request"),
                Map.entry(WarehouseReserveProductsResponseEvent.class, "warehouse-reserve-products-response"),
                Map.entry(WarehouseReleaseProductsResponseEvent.class, "warehouse-release-products-response")
        );

        for (var entry : eventKeyMap.entrySet()) {
            boolean isExist = sagaProperties.getAllTopics().contains(entry.getValue());
            if (!isExist) {
                throw new IllegalStateException("Missing topic in config: " + entry.getValue());
            }
            eventTopicMap.put(entry.getKey(), entry.getValue());
        }
    }

    public String getTopicByEvent(Class<? extends KafkaEvent> eventClass) {
        String topic = eventTopicMap.get(eventClass);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + eventClass.getName());
        }
        return topic;
    }
}