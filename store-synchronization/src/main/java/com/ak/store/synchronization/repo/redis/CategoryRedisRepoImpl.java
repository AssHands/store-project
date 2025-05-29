package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.mapper.CategoryMapper;
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

    private final String CATEGORY_KEY = "category:";
    private final String CATEGORY_CHARACTERISTIC_KEY = "category_characteristic:";
    private final String RELATED_CATEGORY_KEY = "related_category:";

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

        operations.opsForValue().set(CATEGORY_KEY + categoryId, gson.toJson(categoryDocument));
    }

    private void saveAllCategoryCharacteristic(RedisOperations<Object, Object> operations, CategorySnapshotPayload payload) {
        String categoryId = payload.getCategory().getId().toString();

        operations.delete(CATEGORY_CHARACTERISTIC_KEY + categoryId);

        if (payload.getCharacteristics() != null && !payload.getCharacteristics().isEmpty()) {
            operations.delete(CATEGORY_CHARACTERISTIC_KEY + categoryId);
            String[] stringIds = payload.getCharacteristics().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);

            operations.opsForSet().add(CATEGORY_CHARACTERISTIC_KEY + categoryId, stringIds);
        }
    }

    private void saveAllRelatedCategory(RedisOperations<Object, Object> operations, CategorySnapshotPayload payload) {
        String categoryId = payload.getCategory().getId().toString();

        operations.delete(RELATED_CATEGORY_KEY + categoryId);

        if (payload.getRelatedCategories() != null && !payload.getRelatedCategories().isEmpty()) {
            String[] stringIds = payload.getRelatedCategories().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);

            operations.opsForSet().add(RELATED_CATEGORY_KEY + categoryId, stringIds);
        }
    }

    @Override
    public void deleteOne(Long id) {
        stringRedisTemplate.execute(new SessionCallback<Void>() {
            @Override
            public <K, V> Void execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                //delete one category
                operations.delete((K) (CATEGORY_KEY + id));

                //delete all category characteristic
                operations.delete((K) (CATEGORY_CHARACTERISTIC_KEY + id));

                // delete all related category
                operations.delete((K) (RELATED_CATEGORY_KEY + id));

                List<Object> results = operations.exec();
                System.out.println("Transaction results: " + results);

                return null;
            }
        });
    }
}
