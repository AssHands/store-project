package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;

public interface CategoryRedisRepo {
    void saveOne(CategorySnapshotPayload payload);

    void deleteOne(Long id);
}