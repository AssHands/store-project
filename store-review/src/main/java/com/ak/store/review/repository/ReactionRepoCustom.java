package com.ak.store.review.repository;

import com.ak.store.review.model.dto.ReactionRemoveStatus;
import com.ak.store.review.model.dto.ReactionSaveStatus;
import org.bson.types.ObjectId;

import java.util.UUID;

public interface ReactionRepoCustom {
    ReactionSaveStatus likeOneReview(UUID userId, ObjectId reviewId);

    ReactionSaveStatus dislikeOneReview(UUID userId, ObjectId reviewId);

    ReactionRemoveStatus findOneAndRemove(UUID userId, ObjectId reviewId);
}