package com.ak.store.review.validator.service;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.repository.ReactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ReactionServiceValidator {
    private final ReactionRepo reactionRepo;

    public void validateLikingOneReview(UUID userId, String reviewId) {
        var reaction = reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);
    }

    public void validateDislikingOneReview(UUID userId, String reviewId) {

    }

    private boolean isReactionExist(UUID userId, String reviewId) {
        var reaction = reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);

    }

    private Optional<Reaction> findOneByUserIdAndReviewId(UUID userId, String reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);
    }
}
