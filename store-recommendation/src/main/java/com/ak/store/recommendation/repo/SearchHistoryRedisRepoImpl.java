package com.ak.store.recommendation.repo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class SearchHistoryRedisRepoImpl implements SearchHistoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String SEARCH_HISTORY_KEY = "search_history:";

    public SearchHistoryRedisRepoImpl(@Qualifier("stringRedisTemplateForSearch") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<Long> findAllCategoryByConsumerId(String consumerId) {
        Set<String> searchHistory = stringRedisTemplate.opsForSet().members(SEARCH_HISTORY_KEY + consumerId);

        if (searchHistory == null) {
            return Collections.emptyList();
        }

        return searchHistory.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public void putAll(String consumerId, List<Long> categoryIds) {
        String[] ids = categoryIds.stream()
                .map(Object::toString)
                .toArray(String[]::new);

        stringRedisTemplate.opsForSet().add(SEARCH_HISTORY_KEY + consumerId, ids);
    }
}