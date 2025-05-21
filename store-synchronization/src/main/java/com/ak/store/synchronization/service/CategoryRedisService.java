package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.document.CategoryDocument;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.repo.redis.CategoryRedisRepo;
import com.ak.store.synchronization.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    private final CategoryRedisRepo categoryRedisRepo;
    private final CategoryMapper categoryMapper;

    public List<CategoryDocument> createAll(List<CategorySnapshotPayload> request) {
        for(var payload : request) {
            categoryRedisRepo.saveAllCategoryCharacteristic(payload.getCategory().getId(), payload.getCharacteristics());
            categoryRedisRepo.saveAllRelatedCategory(payload.getCategory().getId(), payload.getRelatedCategories());
        }

        var categories = request.stream().map(CategorySnapshotPayload::getCategory).toList();
        return categoryRedisRepo.saveAll(categoryMapper.toCategoryDocument(categories));
    }

    public List<CategoryDocument> updateAll(List<CategorySnapshotPayload> request) {
        for(var payload : request) {
            categoryRedisRepo.saveAllCategoryCharacteristic(payload.getCategory().getId(), payload.getCharacteristics());
            categoryRedisRepo.saveAllRelatedCategory(payload.getCategory().getId(), payload.getRelatedCategories());
        }

        var categories = request.stream().map(CategorySnapshotPayload::getCategory).toList();
        return categoryRedisRepo.saveAll(categoryMapper.toCategoryDocument(categories));
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisRepo.deleteAllById(ids);
    }
}