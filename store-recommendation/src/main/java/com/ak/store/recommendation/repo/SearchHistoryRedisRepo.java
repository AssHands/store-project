package com.ak.store.recommendation.repo;

import java.util.List;
import java.util.Set;

public interface SearchHistoryRedisRepo {
    Set<Long> findAllByConsumerId(String consumerId);
    void putOne(String consumerId, Long categoryId);
    void putAll(String consumerId, List<Long> categoryIds);
}
