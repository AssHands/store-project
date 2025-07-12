package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.kafka.review.ReviewDeletionEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.review.ReviewDeletionSnapshot;
import com.ak.store.reviewOutbox.kafka.EventProducerKafka;
import com.ak.store.reviewOutbox.mapper.JsonMapper;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewDeletionOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapper jsonMapper;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var reviewDeletionEvent = new ReviewDeletionEvent(event.getId(),
                gson.fromJson(event.getPayload(), ReviewDeletionSnapshot.class));

        var request = SagaRequestEvent.builder()
                .sagaId(event.getId())
                .request(jsonMapper.toJsonNode(reviewDeletionEvent.getRequest()))
                .build();

        String reviewId = reviewDeletionEvent.getRequest().getReviewId();
        eventProducerKafka.send(request, getType(), reviewId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_DELETION;
    }
}
