package com.ak.store.review.validator.service;

import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReactionServiceValidator {
    private final ReviewRepo reviewRepo;

    public void validateLikingOneReview(ObjectId reviewId) {
        if (!isReviewExist(reviewId)) {
            throw new RuntimeException("review do not exist");
        }
    }

    public void validateDislikingOneReview(ObjectId reviewId) {
        if (!isReviewExist(reviewId)) {
            throw new RuntimeException("review do not exist");
        }
    }

    private boolean isReviewExist(ObjectId reviewId) {
        return reviewRepo.findById(reviewId).isPresent();
    }
}
