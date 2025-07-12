package com.ak.store.reviewSagaWorker.repository;

import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
import org.bson.types.ObjectId;

public interface ReviewRepoCustom {
    void updateOneStatusById(ObjectId id, ReviewStatus status);
}
