package com.ak.store.review.repository;

import com.ak.store.review.model.document.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment, ObjectId> {
    List<Comment> findAllByReviewId(ObjectId reviewId);
}
