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
        Set<String> historySet = stringRedisTemplate.opsForSet().members(consumerId);

        if (historySet == null) {
            return Collections.emptySet();
        }

        return historySet.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public void putAll(String consumerId, List<Long> categoryIds) {
        String[] ids = (String[]) categoryIds.stream()
                .map(Object::toString)
                .toArray();

        stringRedisTemplate.opsForSet().add(consumerId, ids);
    }
}