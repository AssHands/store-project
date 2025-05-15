package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.document.CategoryDocument;
import com.ak.store.synchronization.repo.redis.CategoryRedisRepo;
import com.ak.store.synchronization.util.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    private final CategoryRedisRepo categoryRedisRepo;
    private final CategoryMapper categoryMapper;

    public List<CategoryDocument> createAll(List<CategoryDTO> categories) {
        for(var category : categories) {
            categoryRedisRepo.saveAllCategoryCharacteristic(category.getId(), category.getCharacteristics());
            categoryRedisRepo.saveAllRelatedCategory(category.getId(), category.getRelatedCategories());
        }

        return categoryRedisRepo.saveAll(categoryMapper.toCategoryDocument(categories));
    }

    public List<CategoryDocument> updateAll(List<CategoryDTO> categories) {
        for(var category : categories) {
            categoryRedisRepo.saveAllCategoryCharacteristic(category.getId(), category.getCharacteristics());
            categoryRedisRepo.saveAllRelatedCategory(category.getId(), category.getRelatedCategories());
        }

        return categoryRedisRepo.saveAll(categoryMapper.toCategoryDocument(categories));
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisRepo.deleteAllById(ids);
    }
}
