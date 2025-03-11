package com.ak.store.warehouse.kafka;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.event.order.OrderEvent;
import com.ak.store.warehouse.facade.WarehouseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final WarehouseFacade warehouseFacade;

    @KafkaListener(topics = "product-created-events", groupId = "warehouse-group")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        warehouseFacade.createProductWarehouse(productCreatedEvent.getProductDocument().getId());
    }
}
