package com.ak.store.review.facade;

import com.ak.store.review.service.ReactionService;
import com.ak.store.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionFacade {
    private final ReactionService reactionService;
    private final ReviewService reviewService;

    public void likeOneReview(UUID userId, String reviewId) {
        Boolean isLike = reactionService.findOneByUserIdAndReviewId(userId, reviewId);

        if(isLike == null) {
            reactionService.likeOneReview(userId, reviewId);
            reviewService.incrementOneLikeAmount(reviewId);
        } else if (!isLike) {
            reactionService.undislikeOneReview(userId, reviewId);
            reviewService.decrementOneDislikeAmount(reviewId);

            reactionService.likeOneReview(userId, reviewId);
            reviewService.incrementOneLikeAmount(reviewId);
        }
    }

    public void unlikeOneReview(UUID userId, String reviewId) {
        boolean isUnliked = reactionService.unlikeOneReview(userId, reviewId);

        if(isUnliked) {
            reviewService.decrementOneLikeAmount(reviewId);
        }
    }

    public void dislikeOneReview(UUID userId, String reviewId) {
        reactionService.dislikeOneReview(userId, reviewId);
        reviewService.incrementOneDislikeAmount(reviewId);
    }

    public void undislikeOneReview(UUID userId, String reviewId) {
        boolean isUndisliked = reactionService.undislikeOneReview(userId, reviewId);

        if(isUndisliked) {
            reviewService.decrementOneDislikeAmount(reviewId);
        }
    }
}