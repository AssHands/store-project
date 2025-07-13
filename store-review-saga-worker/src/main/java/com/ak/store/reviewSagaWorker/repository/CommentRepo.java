package com.ak.store.reviewSagaWorker.repository;

import com.ak.store.reviewSagaWorker.model.document.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends MongoRepository<Comment, ObjectId> {
    void deleteAllByReviewId(ObjectId reviewId);
}
