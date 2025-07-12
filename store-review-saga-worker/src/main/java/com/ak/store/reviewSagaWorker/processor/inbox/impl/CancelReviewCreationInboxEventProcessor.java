package com.ak.store.reviewSagaWorker.processor.inbox.impl;

import com.ak.store.reviewSagaWorker.model.dto.CancelReviewCreationRequest;
import com.ak.store.reviewSagaWorker.model.entity.InboxEvent;
import com.ak.store.reviewSagaWorker.model.entity.InboxEventType;
import com.ak.store.reviewSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.reviewSagaWorker.service.ReviewService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelReviewCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ReviewService reviewService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), CancelReviewCreationRequest.class);
        reviewService.cancelOneCreation(new ObjectId(request.getReviewId()));
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_REVIEW_CREATION;
    }
}
