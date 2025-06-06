package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.event.review.ReviewCreatedEvent;
import com.ak.store.common.model.review.snapshot.ReviewSnapshot;
import com.ak.store.reviewOutbox.kafka.EventProducerKafka;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var reviewCreatedEvent = new ReviewCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ReviewSnapshot.class));

        String reviewId = reviewCreatedEvent.getReview().getId();
        eventProducerKafka.send(reviewCreatedEvent, reviewId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_CREATED;
    }
}