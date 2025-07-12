package com.ak.store.reviewSagaWorker.repository;

import com.ak.store.reviewSagaWorker.model.document.Review;
import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepo extends ReviewRepoCustom, MongoRepository<Review, ObjectId> {
}