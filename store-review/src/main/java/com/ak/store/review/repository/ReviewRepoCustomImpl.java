package com.ak.store.review.repository;

import com.ak.store.review.model.document.Review;
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
    public void incrementOneCommentAmount(String reviewId) {
        var query = new Query(Criteria.where("_id").is(new ObjectId(reviewId)));
        var update = new Update().inc("commentAmount", 1);
        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void decrementOneCommentAmount(String reviewId) {
        var query = new Query(
                Criteria.where("_id").is(new ObjectId(reviewId))
                        .and("commentAmount").gt(0)
        );
        var update = new Update().inc("commentAmount", -1);
        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void incrementOneLikeAmount(String reviewId) {
        var query = new Query(Criteria.where("_id").is(new ObjectId(reviewId)));
        var update = new Update().inc("likeAmount", 1);
        mongoTemplate.updateFirst(query, update, Review.class);
    }

    @Override
    public void incrementOneISLikeAmount(String reviewId) {
        var query = new Query(Criteria.where("_id").is(new ObjectId(reviewId)));
        var update = new Update().inc("dislikeAmount", 1);
        mongoTemplate.updateFirst(query, update, Review.class);
    }
}