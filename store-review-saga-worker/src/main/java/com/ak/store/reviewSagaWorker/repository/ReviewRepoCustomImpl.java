package com.ak.store.reviewSagaWorker.repository;

import com.ak.store.reviewSagaWorker.model.document.Review;
import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
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
    public void updateOneStatusById(ObjectId id, ReviewStatus status) {
        var query = new Query(Criteria.where("_id").is(id));

        var update = new Update().set("status", status);

        mongoTemplate.updateFirst(query, update, Review.class);
    }
}
