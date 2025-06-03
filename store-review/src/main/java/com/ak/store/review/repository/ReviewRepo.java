package com.ak.store.review.repository;

import com.ak.store.review.model.document.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepo extends ReviewRepoCustom, MongoRepository<Review, ObjectId> {
    List<Review> findAllByProductId(Long productId, Pageable pageable);

    Optional<Review> findOneByUserIdAndProductId(UUID userId, Long productId);
}