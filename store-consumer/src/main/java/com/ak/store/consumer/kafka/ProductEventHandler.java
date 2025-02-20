package com.ak.store.consumer.kafka;

import com.ak.store.common.event.ProductDeletedEvent;
import com.ak.store.consumer.facade.CartServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventHandler {
    private final CartServiceFacade cartServiceFacade;

    @KafkaListener(topics = "product-deleted-events", groupId = "consumer-consumer-group")
    public void handle(ProductDeletedEvent productDeletedEvent) {
        cartServiceFacade.deleteAllByProductId(productDeletedEvent.getId());
    }
}