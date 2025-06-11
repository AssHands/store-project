package com.ak.store.synchronization.repository.redis;

import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;
import com.ak.store.synchronization.mapper.CategoryMapper;
import com.ak.store.synchronization.util.CategoryRedisKeys;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryRedisRepoImpl implements CategoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final CategoryMapper categoryMapper;
    private final Gson gson;

    @Override
    public void saveOne(CategorySnapshotPayload payload) {
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                saveOne(operations, payload);
                saveAllCategoryCharacteristic(operations, payload);
                saveAllRelatedCategory(operations, payload);

                return operations.exec();
            }
        });
    }

    private void saveOne(RedisOperations<Object, Object> operations, CategorySnapshotPayload payload) {
        String categoryId = payload.getCategory().getId().toString();
        var categoryDocument = categoryMapper.toCategoryDocument(payload.getCategory());

        operations.opsForValue().set(CategoryRedisKeys.CATEGORY + categoryId, gson.toJson(categoryDocument));
    }

    private void saveAllCategoryCharacteristic(RedisOperations<Object, Object> operations, CategorySnapshotPayload payload) {
        String categoryId = payload.getCategory().getId().toString();

        operations.delete(CategoryRedisKeys.CATEGORY_CHARACTERISTIC + categoryId);

        if (payload.getCharacteristics() != null && !payload.getCharacteristics().isEmpty()) {
            operations.delete(CategoryRedisKeys.CATEGORY_CHARACTERISTIC + categoryId);
            String[] stringIds = payload.getCharacteristics().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);

            operations.opsForSet().add(CategoryRedisKeys.CATEGORY_CHARACTERISTIC + categoryId, stringIds);
        }
    }

    private void saveAllRelatedCategory(RedisOperations<Object, Object> operations, CategorySnapshotPayload payload) {
        String categoryId = payload.getCategory().getId().toString();

        operations.delete(CategoryRedisKeys.RELATED_CATEGORY + categoryId);

        if (payload.getRelatedCategories() != null && !payload.getRelatedCategories().isEmpty()) {
            String[] stringIds = payload.getRelatedCategories().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);

            operations.opsForSet().add(CategoryRedisKeys.RELATED_CATEGORY + categoryId, stringIds);
        }
    }

    @Override
    public void deleteOne(Long id) {
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                operations.delete(CategoryRedisKeys.CATEGORY + id);
                operations.delete(CategoryRedisKeys.CATEGORY_CHARACTERISTIC + id);
                operations.delete(CategoryRedisKeys.RELATED_CATEGORY + id);

                return operations.exec();
            }
        });
    }
}