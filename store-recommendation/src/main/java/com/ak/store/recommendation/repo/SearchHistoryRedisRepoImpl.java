package com.ak.store.recommendation.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class SearchHistoryRedisRepoImpl implements SearchHistoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Long> findAllCategoryByConsumerId(String consumerId) {
        Set<String> searchHistory = stringRedisTemplate.opsForSet().members("search_history:" + consumerId);

        if (searchHistory == null) {
            return Collections.emptyList();
        }

        return searchHistory.stream()
                .map(Long::parseLong)
                .toList();
    }

    @Override
    public List<Long> findAllRelatedCategoryByConsumerId(String consumerId) {
        Set<String> searchHistory = stringRedisTemplate.opsForSet().members("search_history:" + consumerId);

        if (searchHistory == null) {
            return Collections.emptyList();
        }

        Set<String> relatedCategories = new HashSet<>();
        for(var id : searchHistory) {
            Set<String> related = stringRedisTemplate.opsForSet().members("related_category:" + id);

            if(related == null) {
                continue;
            }

            relatedCategories.addAll(related);
        }

         return relatedCategories.stream()
                 .map(Long::parseLong)
                 .toList();
    }

    @Override
    public void putAll(String consumerId, List<Long> categoryIds) {
        String[] ids = (String[]) categoryIds.stream()
                .map(Object::toString)
                .toArray();

        stringRedisTemplate.opsForSet().add("search_history:" + consumerId, ids);
    }
}