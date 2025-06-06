package com.ak.store.review.repository;

import com.ak.store.review.model.document.Reaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReactionRepo extends ReactionRepoCustom, MongoRepository<Reaction, ObjectId> {
    Optional<Reaction> findOneByUserIdAndReviewId(UUID userId, ObjectId reviewId);

    List<Reaction> findAllByUserIdAndReviewIdIn(UUID userId, List<ObjectId> reviewIds);
}