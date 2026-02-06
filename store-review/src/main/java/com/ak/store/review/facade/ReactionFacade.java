package com.ak.store.review.facade;

import com.ak.store.review.model.dto.ReactionDTO;
import com.ak.store.review.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionFacade {
    private final ReactionService reactionService;

    public void likeOne(UUID userId, ObjectId reviewId) {
        reactionService.likeOne(userId, reviewId);
    }

    public void dislikeOne(UUID userId, ObjectId reviewId) {
        reactionService.dislikeOne(userId, reviewId);
    }

    public void removeOne(UUID userId, ObjectId reviewId) {
        reactionService.removeOne(userId, reviewId);
    }

    public List<ReactionDTO> findAllByReviewIds(UUID userId, List<ObjectId> reviewIds) {
        return reactionService.findAllByReviewIds(userId, reviewIds);
    }
}