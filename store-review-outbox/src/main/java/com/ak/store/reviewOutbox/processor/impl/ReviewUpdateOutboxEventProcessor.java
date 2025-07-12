package com.ak.store.reviewOutbox.processor.impl;

import com.ak.store.common.kafka.review.ReviewUpdateEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.review.ReviewUpdateSnapshotPayload;
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
public class ReviewUpdateOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapper jsonMapper;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var reviewUpdateEvent = new ReviewUpdateEvent(event.getId(),
                gson.fromJson(event.getPayload(), ReviewUpdateSnapshotPayload.class));

        var request = SagaRequestEvent.builder()
                .sagaId(event.getId())
                .request(jsonMapper.toJsonNode(reviewUpdateEvent.getRequest()))
                .build();

        String reviewId = reviewUpdateEvent.getRequest().getReview().getId();
        eventProducerKafka.send(request, getType(), reviewId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_UPDATE;
    }
}
