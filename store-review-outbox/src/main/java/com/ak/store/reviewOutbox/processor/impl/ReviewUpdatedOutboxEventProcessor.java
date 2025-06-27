package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.kafka.review.ReviewUpdatedEvent;
import com.ak.store.common.snapshot.review.ReviewUpdatedSnapshotPayload;
import com.ak.store.reviewOutbox.kafka.EventProducerKafka;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var reviewUpdatedEvent = new ReviewUpdatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ReviewUpdatedSnapshotPayload.class));

        String reviewId = reviewUpdatedEvent.getPayload().getReview().getId();
        eventProducerKafka.send(reviewUpdatedEvent, reviewId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_UPDATED;
    }
}
