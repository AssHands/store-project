package com.ak.store.review.validator.service;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.repository.ReactionRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
//todo доделать
public class ReactionServiceValidator {
    private final ReactionRepo reactionRepo;

    public void validateLikingOneReview(UUID userId, ObjectId reviewId) {
        var reaction = reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);
    }

    public void validateDislikingOneReview(UUID userId, ObjectId reviewId) {

    }

    private boolean isReactionExist(UUID userId, ObjectId reviewId) {
        var reaction = reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);
        return true;
    }

    private Optional<Reaction> findOneByUserIdAndReviewId(UUID userId, ObjectId reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId);
    }
}
