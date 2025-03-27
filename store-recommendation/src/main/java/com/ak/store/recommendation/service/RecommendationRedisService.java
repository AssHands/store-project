package com.ak.store.recommendation.service;

import com.ak.store.recommendation.repo.CatalogueRedisRepo;
import com.ak.store.recommendation.repo.SearchHistoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationRedisService {
    private final SearchHistoryRedisRepo searchHistoryRedisRepo;
    private final CatalogueRedisRepo catalogueRedisRepo;

    public List<Long> getAllCategoryId(String consumerId) {
        return searchHistoryRedisRepo.findAllCategoryByConsumerId(consumerId);
    }

    public List<Long> getAllRelatedCategoryId(String consumerId) {
        return catalogueRedisRepo.findAllRelatedCategoryByCategoryIds(
                searchHistoryRedisRepo.findAllCategoryByConsumerId(consumerId)
        );
    }

    public void putAll(String consumerId, List<Long> categoryIds) {
        searchHistoryRedisRepo.putAll(consumerId, categoryIds);
    }
}
