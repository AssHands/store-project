package com.ak.store.review.repository;

import com.ak.store.review.model.document.Reaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReactionRepo extends MongoRepository<Reaction, ObjectId> {
}
