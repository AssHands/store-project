package com.ak.store.warehouse.kafka;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.warehouse.facade.InventoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final InventoryFacade inventoryFacade;

    @KafkaListener(topics = "${kafka.topics.product-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        inventoryFacade.createOne(productCreatedEvent.getPayload().getProduct().getId());
    }
}