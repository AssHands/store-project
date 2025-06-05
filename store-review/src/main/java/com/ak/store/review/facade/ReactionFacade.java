package com.ak.store.review.facade;

import com.ak.store.review.service.ReactionService;
import com.ak.store.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionFacade {
    private final ReactionService reactionService;
    private final ReviewService reviewService;

    public void likeOneReview(UUID userId, ObjectId reviewId) {
        Boolean isLike = reactionService.findOneByUserIdAndReviewId(userId, reviewId);

        if(isLike == null) {
            reactionService.likeOneReview(userId, reviewId);
            reviewService.incrementOneLikeAmount(reviewId);
        } else if (!isLike) {
            reactionService.likeOneReview(userId, reviewId);
            reviewService.incrementOneLikeAmountAndDecrementDislikeAmount(reviewId);
        }
    }

    public void dislikeOneReview(UUID userId, ObjectId reviewId) {
        Boolean isLike = reactionService.findOneByUserIdAndReviewId(userId, reviewId);

        if(isLike == null) {
            reactionService.dislikeOneReview(userId, reviewId);
            reviewService.incrementOneDislikeAmount(reviewId);
        } else if (isLike) {
            reactionService.dislikeOneReview(userId, reviewId);
            reviewService.decrementOneLikeAmountAndIncrementDislikeAmount(reviewId);
        }
    }

    public void removeOneReaction(UUID userId, ObjectId reviewId) {
        Boolean isLiked = reactionService.findOneByUserIdAndReviewId(userId, reviewId);

        if(isLiked) {
            reactionService.removeOneReaction(userId, reviewId);
            reviewService.decrementOneLikeAmount(reviewId);
        } else if (!isLiked) {
            reactionService.removeOneReaction(userId, reviewId);
            reviewService.decrementOneDislikeAmount(reviewId);
        }
    }
}