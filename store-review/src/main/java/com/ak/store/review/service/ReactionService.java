package com.ak.store.review.service;

import com.ak.store.review.mapper.ReactionMapper;
import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.model.dto.ReactionDTO;
import com.ak.store.review.model.dto.ReactionRemoveStatus;
import com.ak.store.review.model.dto.ReactionSaveStatus;
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

    public Boolean findOneByUserIdAndReviewId(UUID userId, ObjectId reviewId) {
        return reactionRepo.findOneByUserIdAndReviewId(userId, reviewId)
                .map(Reaction::getIsLike)
                .orElse(null);
    }

    public ReactionSaveStatus likeOneReview(UUID userId, ObjectId reviewId) {
        reactionValidator.validateLikingOneReview(reviewId);
        return reactionRepo.likeOneReview(userId, reviewId);
    }

    public ReactionSaveStatus dislikeOneReview(UUID userId, ObjectId reviewId) {
        reactionValidator.validateDislikingOneReview(reviewId);
        return reactionRepo.dislikeOneReview(userId, reviewId);
    }

    public ReactionRemoveStatus removeOne(UUID userId, ObjectId reviewId) {
        return reactionRepo.findOneAndRemove(userId, reviewId);
    }

    public List<ReactionDTO> findAllByReviewIds(UUID userId, List<ObjectId> reviewIds) {
        return reactionMapper.toReactionDTO(reactionRepo.findAllByUserIdAndReviewIdIn(userId, reviewIds));
    }
}