package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.kafka.review.ReviewDeletedEvent;
import com.ak.store.common.snapshot.review.ReviewDeletedSnapshot;
import com.ak.store.reviewOutbox.kafka.EventProducerKafka;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewDeletedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var reviewDeletedEvent = new ReviewDeletedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ReviewDeletedSnapshot.class));

        String reviewId = reviewDeletedEvent.getReview().getId();
        eventProducerKafka.send(reviewDeletedEvent, reviewId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_DELETED;
    }
}