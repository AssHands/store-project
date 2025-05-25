package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.document.CategoryDocument;

import java.util.List;

public interface CategoryRedisRepo {
    void saveOne(CategoryDocument category);

    void saveAllCategoryCharacteristic(Long categoryId, List<Long> characteristicIds);

    void saveAllRelatedCategory(Long categoryId, List<Long> relatedCategories);

    void deleteOne(Long id);
}