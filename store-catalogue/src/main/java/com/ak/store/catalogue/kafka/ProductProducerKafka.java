package com.ak.store.catalogue.kafka;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducerKafka {
    private final KafkaTemplate<String, ProductEvent> kafkaProductTemplate;

    public void send(ProductDeletedEvent productDeletedEvent) {
        try {
            SendResult<String, ProductEvent> future = kafkaProductTemplate
                    .send("product-deleted-events", productDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka product-deleted-events error");
        }
    }

}