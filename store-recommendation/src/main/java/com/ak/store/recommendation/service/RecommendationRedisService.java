package com.ak.store.recommendation.service;

import com.ak.store.recommendation.repo.CatalogueRedisRepo;
import com.ak.store.recommendation.repo.SearchHistoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RecommendationRedisService {
    private final SearchHistoryRedisRepo searchHistoryRedisRepo;
    private final CatalogueRedisRepo catalogueRedisRepo;

    public List<Long> getAllCategoryId(UUID userId) {
        return searchHistoryRedisRepo.findAllCategoryByUserId(userId);
    }

    public List<Long> getAllRelatedCategoryId(UUID userId) {
        return catalogueRedisRepo.findAllRelatedCategoryByCategoryIds(
                searchHistoryRedisRepo.findAllCategoryByUserId(userId)
        );
    }

    public void putAll(UUID userId, List<Long> categoryIds) {
        searchHistoryRedisRepo.putAll(userId, categoryIds);
    }
}
