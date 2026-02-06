package com.ak.store.review.repository.impl;

import com.ak.store.review.model.document.Review;
import com.ak.store.review.repository.ReviewRepoCustom;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReviewRepoCustomImpl implements ReviewRepoCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public void incrementCommentAmount(ObjectId reviewId) {
        var query = new Query(Criteria.where("_id").is(reviewId));

        var update = new Update().inc("commentAmount", 1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void decrementCommentAmount(ObjectId reviewId) {
        var query = new Query(
                Criteria.where("_id").is(reviewId)
                        .and("commentAmount").gt(0)
        );

        var update = new Update().inc("commentAmount", -1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void incrementLikeAmount(ObjectId reviewId) {
        var query = new Query(Criteria.where("_id").is(reviewId));

        var update = new Update().inc("likeAmount", 1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void decrementLikeAmount(ObjectId reviewId) {
        var query = new Query(
                Criteria.where("_id").is(reviewId)
                        .and("likeAmount").gt(0)
        );

        var update = new Update().inc("likeAmount", -1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void incrementDislikeAmount(ObjectId reviewId) {
        var query = new Query(Criteria.where("_id").is(reviewId));

        var update = new Update().inc("dislikeAmount", 1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void decrementDislikeAmount(ObjectId reviewId) {
        var query = new Query(
                Criteria.where("_id").is(reviewId)
                        .and("dislikeAmount").gt(0)
        );

        var update = new Update().inc("dislikeAmount", -1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void incrementLikeAmountAndDecrementDislikeAmount(ObjectId reviewId) {
        var query = new Query(
                Criteria.where("_id").is(reviewId)
                        .and("dislikeAmount").gt(0)
        );

        var update = new Update()
                .inc("likeAmount", 1)
                .inc("dislikeAmount", -1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void decrementLikeAmountAndIncrementDislikeAmount(ObjectId reviewId) {
        var query = new Query(
                Criteria.where("_id").is(reviewId)
                        .and("likeAmount").gt(0)
        );

        var update = new Update()
                .inc("likeAmount", -1)
                .inc("dislikeAmount", 1);

        mongoTemplate.updateFirst(query, update, Review.class);
    }
}