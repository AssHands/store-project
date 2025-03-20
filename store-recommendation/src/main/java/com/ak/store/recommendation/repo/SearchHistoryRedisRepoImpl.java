package com.ak.store.recommendation.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class SearchHistoryRedisRepoImpl implements SearchHistoryRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Set<Long> findAllByConsumerId(String consumerId) {
        var set = stringRedisTemplate.opsForSet().members(consumerId);

        if (set == null) {
            return Collections.emptySet();
        }

        return set.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public void putOne(String consumerId, Long categoryId) {
        stringRedisTemplate.opsForSet().add(consumerId, categoryId.toString());
    }

    @Override
    public void putAll(String consumerId, List<Long> categoryIds) {
        List<String> ids = categoryIds.stream().map(Object::toString).toList();
        stringRedisTemplate.opsForSet().add(consumerId, ids.toArray(new String[0]));
    }
}
