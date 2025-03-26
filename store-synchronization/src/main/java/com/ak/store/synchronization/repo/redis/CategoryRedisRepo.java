package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.document.CategoryDocument;

import java.util.List;

public interface CategoryRedisRepo {
    CategoryDocument save(CategoryDocument category);

    List<CategoryDocument> saveAll(List<CategoryDocument> categories);

    void saveAllCategoryCharacteristic(Long categoryId, List<Long> characteristicIds);

    void deleteAllById(List<Long> ids);
}
