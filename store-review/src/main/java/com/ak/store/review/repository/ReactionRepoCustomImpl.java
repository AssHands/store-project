package com.ak.store.review.repository;

import com.ak.store.review.model.document.Reaction;
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
    public void saveOrUpdate(UUID userId, ObjectId reviewId, boolean isLike) {
        var query = new Query(
                Criteria.where("reviewId").is(reviewId)
                        .and("userId").is(userId)
        );

        var update = new Update();
        update.set("reviewId", reviewId);
        update.set("userId", userId);
        update.set("isLike", isLike);

        mongoTemplate.upsert(query, update, Reaction.class);
    }
}