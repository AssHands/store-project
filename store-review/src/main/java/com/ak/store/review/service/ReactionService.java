package com.ak.store.review.service;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.repository.ReactionRepo;
import com.ak.store.review.validator.service.ReactionServiceValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ReactionServiceValidator reactionValidator;
    private final ReactionRepo reactionRepo;

    public Boolean findOneByUserIdAndReviewId(UUID userId, ObjectId reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId)
                .map(Reaction::getIsLike)
                .orElse(null);
    }

    public void likeOneReview(UUID userId, ObjectId reviewId) {
        //reactionValidator.validateLikingOneReview(userId, reviewId);
        reactionRepo.saveOrUpdate(userId, reviewId, true);
    }

    public void dislikeOneReview(UUID userId, ObjectId reviewId) {
        //reactionValidator.validateDislikingOneReview(userId, reviewId);
        reactionRepo.saveOrUpdate(userId, reviewId, false);
    }

    public void removeOneReaction(UUID userId, ObjectId reviewId) {
        reactionRepo.deleteOneByUserIdAndReviewId(userId, reviewId);
    }

    public void save(ObjectId reviewId) {
        var id = UUID.randomUUID();
        System.out.println(id);

        reactionRepo.save(Reaction.builder()
                        .isLike(false)
                        .userId(id)
                        .reviewId(reviewId)
                .build());
    }
}