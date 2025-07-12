package com.ak.store.reviewSagaWorker.processor.inbox.impl;

import com.ak.store.reviewSagaWorker.model.dto.ConfirmReviewDeletionRequest;
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
public class ConfirmReviewDeletionInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ReviewService reviewService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), ConfirmReviewDeletionRequest.class);
        reviewService.deleteOne(new ObjectId(request.getReviewId()));
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_REVIEW_DELETION;
    }
}
