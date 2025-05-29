package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.repo.redis.CategoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    private final CategoryRedisRepo categoryRedisRepo;

    public void createOne(CategorySnapshotPayload request) {
        categoryRedisRepo.saveOne(request);
    }

    public void updateOne(CategorySnapshotPayload request) {
        categoryRedisRepo.saveOne(request);
    }

    public void deleteOne(Long id) {
        categoryRedisRepo.deleteOne(id);
    }
}