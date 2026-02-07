package com.ak.store.review.repository;

import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.document.ReviewStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepo extends MongoRepository<Review, ObjectId> {
    List<Review> findAllByProductIdAndStatus(Long productId, ReviewStatus status);

    Optional<Review> findOneByUserIdAndProductId(UUID userId, Long productId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'likeAmount': ?1, 'dislikeAmount': ?2 } }")
    void updateReactionCounter(ObjectId reviewId, int likeDelta, int dislikeDelta);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'commentAmount': ?1 } }")
    void updateCommentCounter(ObjectId reviewId, int commentDelta);
}