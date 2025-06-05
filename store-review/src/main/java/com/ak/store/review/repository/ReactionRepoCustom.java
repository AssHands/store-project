package com.ak.store.review.repository;

import org.bson.types.ObjectId;

import java.util.UUID;

public interface ReactionRepoCustom {
    void saveOrUpdate(UUID userId, ObjectId reviewId, boolean isLike);
}