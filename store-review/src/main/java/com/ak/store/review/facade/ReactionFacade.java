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
        reactionService.likeOneReview(userId, reviewId);
        reviewService.incrementOneLikeAmount(reviewId);
    }

    public void dislikeOneReview(UUID userId, String reviewId) {
        reactionService.dislikeOneReview(userId, reviewId);
        reviewService.incrementOneDislikeAmount(reviewId);
    }
}