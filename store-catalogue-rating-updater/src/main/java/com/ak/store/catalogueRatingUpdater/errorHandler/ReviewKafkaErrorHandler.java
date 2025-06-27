package com.ak.store.catalogueRatingUpdater.errorHandler;

import com.ak.store.catalogueRatingUpdater.kafka.producer.DltProducerKafka;
import com.ak.store.common.kafka.review.ReviewCreatedEvent;
import com.ak.store.common.kafka.review.ReviewDeletedEvent;
import com.ak.store.common.kafka.review.ReviewUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleCreateError(ReviewCreatedEvent event, Exception e) {
        dltProducerKafka.send(event);
    }

    public void handleUpdateError(ReviewUpdatedEvent event, Exception e) {
        dltProducerKafka.send(event);
    }

    public void handleDeleteError(ReviewDeletedEvent event, Exception e) {
        dltProducerKafka.send(event);
    }
}
