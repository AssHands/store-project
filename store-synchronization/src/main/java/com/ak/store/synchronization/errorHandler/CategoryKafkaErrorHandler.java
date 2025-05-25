package com.ak.store.synchronization.errorHandler;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
import com.ak.store.common.event.catalogue.CategoryUpdatedEvent;
import com.ak.store.synchronization.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleCreateError(CategoryCreatedEvent event, Exception e) {
        String categoryId = event.getPayload().getCategory().getId().toString();
        dltProducerKafka.send(event, categoryId);
    }

    public void handleUpdateError(CategoryUpdatedEvent event, Exception e) {
        String categoryId = event.getPayload().getCategory().getId().toString();
        dltProducerKafka.send(event, categoryId);
    }

    public void handleDeleteError(CategoryDeletedEvent event, Exception e) {
        String categoryId = event.getPayload().getCategory().getId().toString();
        dltProducerKafka.send(event, categoryId);
    }
}
