package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.document.CategoryDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryRedisRepoImpl implements CategoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;

    private final String CATEGORY_KEY = "category:";
    private final String CATEGORY_CHARACTERISTIC_KEY = "category_characteristic:";
    private final String RELATED_CATEGORY_KEY = "related_category:";

    @Override
    public CategoryDocument saveOne(CategoryDocument category) {
        stringRedisTemplate.opsForValue().set(CATEGORY_KEY + category.getId(), gson.toJson(category));
        return category;
    }

    @Override
    public List<CategoryDocument> saveAll(List<CategoryDocument> categories) {
        for(CategoryDocument category : categories) {
            stringRedisTemplate.opsForValue().set(CATEGORY_KEY + category.getId(), gson.toJson(category));
        }
        return categories;
    }

    @Override
    public void saveAllCategoryCharacteristic(Long categoryId, List<Long> characteristicIds) {
        stringRedisTemplate.delete(CATEGORY_CHARACTERISTIC_KEY + categoryId);

        if(characteristicIds == null || characteristicIds.isEmpty()) {
            stringRedisTemplate.delete(CATEGORY_CHARACTERISTIC_KEY + categoryId);
            return;
        }

        String[] stringIds = characteristicIds.stream()
                .map(Object::toString)
                .toArray(String[]::new);

        stringRedisTemplate.opsForSet().add(CATEGORY_CHARACTERISTIC_KEY + categoryId, stringIds);
    }

    @Override
    public void saveAllRelatedCategory(Long categoryId, List<Long> relatedCategories) {
        stringRedisTemplate.delete(RELATED_CATEGORY_KEY + categoryId);

        if(relatedCategories == null || relatedCategories.isEmpty()) {
            stringRedisTemplate.delete(RELATED_CATEGORY_KEY + categoryId);
            return;
        }

        String[] stringIds = relatedCategories.stream()
                .map(Object::toString)
                .toArray(String[]::new);

        stringRedisTemplate.opsForSet().add(RELATED_CATEGORY_KEY + categoryId, stringIds);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        for(Long id : ids) {
            stringRedisTemplate.delete(CATEGORY_KEY + id);
        }
    }
}
