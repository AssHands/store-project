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

    public List<Long> getAllCategoryId(String consumerId) {
        return searchHistoryRedisRepo.findAllCategoryByConsumerId(consumerId);
    }

    public List<Long> getAllRelatedCategoryId(String consumerId) {
        return searchHistoryRedisRepo.findAllCategoryByConsumerId(consumerId);
    }

    public void putAll(String consumerId, List<Long> categoryIds) {
        searchHistoryRedisRepo.putAll(consumerId, categoryIds);
    }
}
