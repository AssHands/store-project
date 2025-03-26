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
    private final static Gson gson = new Gson();

    @Override
    public CategoryDocument save(CategoryDocument category) {
        stringRedisTemplate.opsForValue().set("category:" + category.getId(), gson.toJson(category));
        return category;
    }

    @Override
    public List<CategoryDocument> saveAll(List<CategoryDocument> categories) {
        for(CategoryDocument category : categories) {
            stringRedisTemplate.opsForValue().set("category:" + category.getId(), gson.toJson(category));
        }
        return categories;
    }

    @Override
    public void saveAllCategoryCharacteristic(Long categoryId, List<Long> characteristicIds) {
        stringRedisTemplate.delete("category_characteristic:" + categoryId);

        String[] stringIds = characteristicIds.stream()
                .map(Object::toString)
                .toArray(String[]::new);

        stringRedisTemplate.opsForSet().add("category_characteristic:" + categoryId, stringIds);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        for(Long id : ids) {
            stringRedisTemplate.delete("category:" + id);
        }
    }
}
