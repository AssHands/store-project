package com.ak.store.review.service;

import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.outbox.OutboxEventService;
import com.ak.store.review.outbox.OutboxEventType;
import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewOutboxService {
    private final ReviewRepo reviewRepo;
    private final OutboxEventService outboxEventService;
    private final ReviewMapper reviewMapper;

    public void saveCreatedEvent(ObjectId id) {
        var review = findOne(id);
        var snapshot = reviewMapper.toSnapshot(review);
        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_CREATED);
    }

    public void saveUpdatedEvent(ObjectId id) {
        var review = findOne(id);
        var snapshot = reviewMapper.toSnapshot(review);
        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_UPDATED);
    }

    public void saveDeletedEvent(ObjectId id) {
        var review = findOne(id);
        var snapshot = reviewMapper.toSnapshot(review);
        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_DELETED);
    }

    private Review findOne(ObjectId id) {
        return reviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("review not found"));
    }
}
