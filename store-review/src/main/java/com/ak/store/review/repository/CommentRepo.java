package com.ak.store.review.repository;

import com.ak.store.review.model.document.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<Comment, ObjectId> {
    List<Comment> findAllByReviewId(String reviewId, Pageable pageable);

    void deleteAllByReviewId(String reviewId);
}
