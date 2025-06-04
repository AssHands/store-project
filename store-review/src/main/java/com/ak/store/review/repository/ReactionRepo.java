package com.ak.store.review.repository;

import com.ak.store.review.model.document.Reaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepo extends MongoRepository<Reaction, ObjectId> {
    long deleteOneByUserIdAndReviewId(UUID userId, String reviewId);

    Optional<Reaction> findOneByUserIdAndReviewId(UUID userId, String reviewId);
}
