package com.ak.store.review.service;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.repository.ReactionRepo;
import com.ak.store.review.validator.service.ReactionServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ReactionServiceValidator reactionValidator;
    private final ReactionRepo reactionRepo;

    public Boolean findOneByUserIdAndReviewId(UUID userId, String reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId)
                .map(Reaction::getIsLike)
                .orElse(null);
    }

    public void likeOneReview(UUID userId, String reviewId) {
        reactionValidator.validateLikingOneReview(userId, reviewId);

        var reaction = Reaction.builder()
                .userId(userId)
                .reviewId(reviewId)
                .isLike(true)
                .build();

        reactionRepo.save(reaction);
    }

    public boolean unlikeOneReview(UUID userId, String reviewId) {
        long result = reactionRepo.deleteOneByUserIdAndReviewId(userId, reviewId);
        return result > 0;
    }

    public void dislikeOneReview(UUID userId, String reviewId) {
        reactionValidator.validateDislikingOneReview(userId, reviewId);

        var reaction = Reaction.builder()
                .userId(userId)
                .reviewId(reviewId)
                .isLike(false)
                .build();

        reactionRepo.save(reaction);
    }

    public boolean undislikeOneReview(UUID userId, String reviewId) {
        long result = reactionRepo.deleteOneByUserIdAndReviewId(userId, reviewId);
        return result > 0;
    }
}