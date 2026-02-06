package com.ak.store.review.service;

import com.ak.store.review.mapper.ReactionMapper;
import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.model.dto.ReactionDTO;
import com.ak.store.review.repository.ReactionRepo;
import com.ak.store.review.validator.service.ReactionServiceValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ReactionServiceValidator reactionValidator;
    private final ReactionRepo reactionRepo;
    private final ReactionMapper reactionMapper;

    private final ReviewService reviewService;

    public Boolean findOneByUserIdAndReviewId(UUID userId, ObjectId reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId)
                .map(Reaction::getIsLike)
                .orElse(null);
    }

    public void likeOne(UUID userId, ObjectId reviewId) {
        reactionValidator.validateLike(reviewId);
        var saveStatus = reactionRepo.likeOne(userId, reviewId);

        switch (saveStatus) {
            case CREATED -> reviewService.incrementLikeAmount(reviewId);
            case UPDATED -> reviewService.incrementLikeAmountAndDecrementDislikeAmount(reviewId);
        }
    }

    public void dislikeOne(UUID userId, ObjectId reviewId) {
        reactionValidator.validateDislike(reviewId);
        var saveStatus = reactionRepo.dislikeOne(userId, reviewId);

        switch (saveStatus) {
            case CREATED -> reviewService.incrementDislikeAmount(reviewId);
            case UPDATED -> reviewService.decrementLikeAmountAndIncrementDislikeAmount(reviewId);
        }
    }

    public void removeOne(UUID userId, ObjectId reviewId) {
        var removeStatus = reactionRepo.findOneAndRemove(userId, reviewId);

        switch (removeStatus) {
            case LIKE_REMOVED -> reviewService.decrementLikeAmount(reviewId);
            case DISLIKE_REMOVED -> reviewService.decrementDislikeAmount(reviewId);
        }
    }

    public List<ReactionDTO> findAllByReviewIds(UUID userId, List<ObjectId> reviewIds) {
        return reactionRepo.findAllByUserIdAndReviewIdIn(userId, reviewIds).stream()
                .map(reactionMapper::toDTO)
                .toList();
    }
}