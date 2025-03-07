package com.ak.store.order.kafka;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.order.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {

    private final OrderFacade orderFacade;

    @KafkaListener(topics = "product-deleted-events", groupId = "order-orders-group")
    //todo: разные транзакции? чё делать
    public void handle(ProductDeletedEvent productDeletedEvent) {
        orderFacade.deleteAllByProductId(productDeletedEvent.getId());
    }
}