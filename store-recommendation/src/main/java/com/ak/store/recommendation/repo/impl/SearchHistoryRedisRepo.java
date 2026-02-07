package com.ak.store.recommendation.repo.impl;

import com.ak.store.recommendation.repo.SearchHistoryRepo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SearchHistoryRedisRepo implements SearchHistoryRepo {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String SEARCH_HISTORY_KEY = "search_history:";

    private final int SEARCH_HISTORY_SIZE = 5;

    @Override
    public List<Long> findOne(UUID userId) {
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
    public void putOne(UUID userId, List<Long> categoryIds) {
        String key = SEARCH_HISTORY_KEY + userId;
        double timestamp = System.currentTimeMillis() / 1000.0;

        Set<ZSetOperations.TypedTuple<String>> tuples = categoryIds.stream()
                .map(Object::toString)
                .map(categoryId -> new DefaultTypedTuple<>(categoryId, timestamp))
                .collect(Collectors.toSet());

        stringRedisTemplate.opsForZSet().add(key, tuples);

        Long currentSize = stringRedisTemplate.opsForZSet().zCard(key);
        if (currentSize != null && currentSize > SEARCH_HISTORY_SIZE) {
            long elementsToRemove = currentSize - SEARCH_HISTORY_SIZE;
            stringRedisTemplate.opsForZSet().removeRange(key, 0, elementsToRemove - 1);
        }
    }
}