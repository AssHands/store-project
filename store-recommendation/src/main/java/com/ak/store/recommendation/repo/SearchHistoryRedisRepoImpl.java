package com.ak.store.recommendation.repo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SearchHistoryRedisRepoImpl implements SearchHistoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String SEARCH_HISTORY_KEY = "search_history:";

    public SearchHistoryRedisRepoImpl(@Qualifier("stringRedisTemplateForSearch") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<Long> findAllCategoryByUserId(UUID userId) {
        Set<String> searchHistory = stringRedisTemplate.opsForZSet()
                .distinctRandomMembers(SEARCH_HISTORY_KEY + userId, 5);

        if (searchHistory == null) {
            return Collections.emptyList();
        }

        return searchHistory.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public void putAll(UUID userId, List<Long> categoryIds) {
        String key = SEARCH_HISTORY_KEY + userId;
        double timestamp = System.currentTimeMillis() / 1000.0;

        Set<ZSetOperations.TypedTuple<String>> tuples = categoryIds.stream()
                .map(Object::toString)
                .map(categoryId -> new DefaultTypedTuple<>(categoryId, timestamp))
                .collect(Collectors.toSet());

        stringRedisTemplate.opsForZSet().add(key, tuples);

        //todo: вынести определение size в другое место
        Long currentSize = stringRedisTemplate.opsForZSet().zCard(key);
        if (currentSize != null && currentSize > 5) {
            long elementsToRemove = currentSize - 5;
            stringRedisTemplate.opsForZSet().removeRange(key, 0, elementsToRemove - 1);
        }
    }
}