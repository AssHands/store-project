package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.mapper.CategoryMapper;
import com.ak.store.synchronization.repo.redis.CategoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    private final CategoryRedisRepo categoryRedisRepo;
    private final CategoryMapper categoryMapper;

    public void createOne(CategorySnapshotPayload request) {
        Long categoryId = request.getCategory().getId();

        categoryRedisRepo.saveOne(categoryMapper.toCategoryDocument(request.getCategory()));
        categoryRedisRepo.saveAllCategoryCharacteristic(categoryId, request.getCharacteristics());
        categoryRedisRepo.saveAllRelatedCategory(categoryId, request.getRelatedCategories());
    }

    public void updateOne(CategorySnapshotPayload request) {
        Long categoryId = request.getCategory().getId();

        categoryRedisRepo.saveOne(categoryMapper.toCategoryDocument(request.getCategory()));
        categoryRedisRepo.saveAllCategoryCharacteristic(categoryId, request.getCharacteristics());
        categoryRedisRepo.saveAllRelatedCategory(categoryId, request.getRelatedCategories());
    }

    public void deleteOne(Long id) {
        categoryRedisRepo.deleteOne(id);
    }
}