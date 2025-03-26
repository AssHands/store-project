package com.ak.store.recommendation.repo;

import java.util.List;

public interface SearchHistoryRedisRepo {
    List<Long> findAllCategoryByConsumerId(String consumerId);

    List<Long> findAllRelatedCategoryByConsumerId(String consumerId);

    void putAll(String consumerId, List<Long> categoryIds);
}
