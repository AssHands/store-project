package com.ak.store.recommendation.repo;

import java.util.List;
import java.util.UUID;

public interface SearchHistoryRedisRepo {
    List<Long> findAllCategoryByUserId(UUID userId);

    void putAll(UUID userId, List<Long> categoryIds);
}
