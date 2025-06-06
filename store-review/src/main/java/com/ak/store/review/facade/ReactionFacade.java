package com.ak.store.review.facade;

import com.ak.store.review.model.dto.ReactionDTO;
import com.ak.store.review.service.ReactionService;
import com.ak.store.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionFacade {
    private final ReactionService reactionService;
    private final ReviewService reviewService;

    public void likeOneReview(UUID userId, ObjectId reviewId) {
        var saveStatus = reactionService.likeOneReview(userId, reviewId);

        switch (saveStatus) {
            case CREATED -> reviewService.incrementOneLikeAmount(reviewId);
            case UPDATED -> reviewService.incrementOneLikeAmountAndDecrementDislikeAmount(reviewId);
        }
    }

    public void dislikeOneReview(UUID userId, ObjectId reviewId) {
        var saveStatus = reactionService.dislikeOneReview(userId, reviewId);

        switch (saveStatus) {
            case CREATED -> reviewService.incrementOneDislikeAmount(reviewId);
            case UPDATED -> reviewService.decrementOneLikeAmountAndIncrementDislikeAmount(reviewId);
        }
    }

    public void removeOne(UUID userId, ObjectId reviewId) {
        var removeStatus = reactionService.removeOne(userId, reviewId);

        switch (removeStatus) {
            case LIKE_REMOVED -> reviewService.decrementOneLikeAmount(reviewId);
            case DISLIKE_REMOVED -> reviewService.decrementOneDislikeAmount(reviewId);
        }
    }

    public List<ReactionDTO> findAllByReviewIds(UUID userId, List<ObjectId> reviewIds) {
        return reactionService.findAllByReviewIds(userId, reviewIds);
    }
}