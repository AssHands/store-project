package com.ak.store.recommendation.service;

import com.ak.store.recommendation.repo.SearchHistoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SearchHistoryRedisService {
    private final SearchHistoryRedisRepo searchHistoryRedisRepo;

    public Set<Long> getAllCategoryId(String consumerId) {
        return searchHistoryRedisRepo.findAllByConsumerId(consumerId);
    }

    public void putOne(String consumerId, Long categoryId) {
        searchHistoryRedisRepo.putOne(consumerId, categoryId);
    }

    public void putAll(String consumerId, List<Long> categoryIds) {
        searchHistoryRedisRepo.putAll(consumerId, categoryIds);
    }
}
