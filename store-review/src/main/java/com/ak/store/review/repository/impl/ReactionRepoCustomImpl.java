package com.ak.store.review.repository.impl;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.model.dto.ReactionRemoveStatus;
import com.ak.store.review.model.dto.ReactionSaveStatus;
import com.ak.store.review.repository.ReactionRepoCustom;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ReactionRepoCustomImpl implements ReactionRepoCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public ReactionSaveStatus likeOne(UUID userId, ObjectId reviewId) {
        var query = new Query(
                Criteria.where("reviewId").is(reviewId)
                        .and("userId").is(userId)
        );

        var update = new Update();
        update.set("reviewId", reviewId);
        update.set("userId", userId);
        update.set("isLike", true);

        var result = mongoTemplate.upsert(query, update, Reaction.class);
        return defineSaveStatus(result);
    }

    @Override
    public ReactionSaveStatus dislikeOne(UUID userId, ObjectId reviewId) {
        var query = new Query(
                Criteria.where("reviewId").is(reviewId)
                        .and("userId").is(userId)
        );

        var update = new Update();
        update.set("reviewId", reviewId);
        update.set("userId", userId);
        update.set("isLike", false);

        var result = mongoTemplate.upsert(query, update, Reaction.class);
        return defineSaveStatus(result);
    }

    @Override
    public ReactionRemoveStatus findOneAndRemove(UUID userId, ObjectId reviewId) {
        Query query = new Query(
                Criteria.where("reviewId").is(reviewId)
                        .and("userId").is(userId)
        );

        var removedReaction = mongoTemplate.findAndRemove(query, Reaction.class);
        return defineRemoveStatus(removedReaction);
    }

    private ReactionSaveStatus defineSaveStatus(UpdateResult result) {
        if (result.getUpsertedId() != null) {
            return ReactionSaveStatus.CREATED;
        } else if (result.getModifiedCount() > 0) {
            return ReactionSaveStatus.UPDATED;
        } else {
            return ReactionSaveStatus.UNCHANGED;
        }
    }

    private ReactionRemoveStatus defineRemoveStatus(Reaction removedReaction) {
        if(removedReaction == null) {
            return ReactionRemoveStatus.NOTHING_REMOVED;
        } else if (removedReaction.getIsLike()) {
            return ReactionRemoveStatus.LIKE_REMOVED;
        } else {
            return ReactionRemoveStatus.DISLIKE_REMOVED;
        }
    }
}