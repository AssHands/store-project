package com.ak.store.synchronization.repository.redis;

import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;

public interface CategoryRedisRepo {

    //todo поменять Snapshot на Document
    void saveOne(CategorySnapshotPayload payload);

    void deleteOne(Long id);
}