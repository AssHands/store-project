package com.ak.store.consumer.kafka;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.consumer.facade.CartServiceFacade;
import com.ak.store.consumer.facade.ReviewServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final CartServiceFacade cartServiceFacade;
    private final ReviewServiceFacade reviewServiceFacade;

    @KafkaListener(topics = "product-deleted-events", groupId = "consumer-consumer-group")
    //todo: разные транзакции? чё делать
    public void handle(ProductDeletedEvent productDeletedEvent) {
        cartServiceFacade.deleteAllByProductId(productDeletedEvent.getId());
        reviewServiceFacade.deleteAllByProductId(productDeletedEvent.getId());
    }
}