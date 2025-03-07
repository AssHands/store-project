package com.ak.store.consumer.kafka;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.consumer.facade.CartFacade;
import com.ak.store.consumer.facade.ReviewFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final CartFacade cartFacade;
    private final ReviewFacade reviewFacade;

    @KafkaListener(topics = "product-deleted-events", groupId = "consumer-consumers-group")
    //todo: разные транзакции? чё делать
    public void handle(ProductDeletedEvent productDeletedEvent) {
        cartFacade.deleteAllByProductId(productDeletedEvent.getId());
        reviewFacade.deleteAllByProductId(productDeletedEvent.getId());
    }
}