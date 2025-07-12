package com.ak.store.reviewSagaWorker.processor.inbox.impl;

import com.ak.store.reviewSagaWorker.mapper.ReviewMapper;
import com.ak.store.reviewSagaWorker.model.dto.CancelReviewUpdateRequest;
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
public class CancelReviewUpdateInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), CancelReviewUpdateRequest.class);
        ObjectId reviewId = new ObjectId(request.getReview().getId());
        reviewService.cancelOneUpdate(reviewId, reviewMapper.toReviewWriteDTO(request.getReview()));
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_REVIEW_UPDATE;
    }
}
