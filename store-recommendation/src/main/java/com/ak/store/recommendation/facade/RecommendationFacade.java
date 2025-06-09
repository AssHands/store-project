package com.ak.store.recommendation.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.recommendation.service.RecommendationElasticService;
import com.ak.store.recommendation.service.RecommendationRedisService;
import com.ak.store.recommendation.model.view.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RecommendationFacade {
    private final RecommendationRedisService recommendationRedisService;
    private final RecommendationElasticService recommendationElasticService;

    public void putInSearchHistory(List<SearchAllEvent> searchAllEvents) {
        Map<UUID, List<Long>> map = new HashMap<>(searchAllEvents.size());

        for (var searchEvent : searchAllEvents) {
            UUID userId = searchEvent.getUserId();
            Long categoryId = searchEvent.getCategoryId();
            map.computeIfAbsent(userId, k -> new ArrayList<>()).add(categoryId);
        }

        for (var entry : map.entrySet()) {
            recommendationRedisService.putAll(entry.getKey(), entry.getValue());
        }
    }

    public RecommendationResponse getRecommendation(UUID userId) {
        if (userId == null) {
            return recommendationElasticService.getRecommendation();
        }

        var relatedCategories = recommendationRedisService.getAllRelatedCategoryId(userId);
        return recommendationElasticService.getRecommendation(relatedCategories);
    }

    public List<Long> findSearchHistory(UUID userId) {
        return recommendationRedisService.getAllCategoryId(userId);
    }
}