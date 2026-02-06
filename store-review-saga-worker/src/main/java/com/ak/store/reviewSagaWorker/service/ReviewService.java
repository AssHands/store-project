package com.ak.store.reviewSagaWorker.service;

import com.ak.store.reviewSagaWorker.model.document.Review;
import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
import com.ak.store.reviewSagaWorker.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void updateOneStatus(ObjectId id, ReviewStatus status) {
        reviewRepo.updateOneStatusById(id, status);
    }

    public void cancelOneCreated(ObjectId id) {
        reviewRepo.deleteById(id);
    }

    public void deleteOne(ObjectId id) {
        reviewRepo.deleteById(id);
    }
}