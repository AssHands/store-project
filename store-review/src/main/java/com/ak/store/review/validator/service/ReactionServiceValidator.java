package com.ak.store.review.validator.service;

import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReactionServiceValidator {
    private final ReviewRepo reviewRepo;

    public void validateLike(ObjectId reviewId) {
        reviewExist(reviewId);
    }

    public void validateDislike(ObjectId reviewId) {
        reviewExist(reviewId);
    }

    private void reviewExist(ObjectId reviewId) {
        if (reviewRepo.findById(reviewId).isPresent()) {
            throw new RuntimeException("review do not exist");
        }
    }
}
