package com.ak.store.recommendation.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.common.snapshot.recommendation.RecommendationResponse;
import com.ak.store.recommendation.service.RecommendationElasticService;
import com.ak.store.recommendation.service.RecommendationRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RecommendationFacade {
    private final RecommendationRedisService recommendationRedisService;
    private final RecommendationElasticService recommendationElasticService;

    public void putInSearchHistory(List<SearchAllEvent> searchAllEvents) {
        Map<String, List<Long>> map = new HashMap<>(searchAllEvents.size());

        for (var searchEvent : searchAllEvents) {
            String consumerId = searchEvent.getConsumerSearch().getConsumerId();
            Long categoryId = searchEvent.getConsumerSearch().getCategoryId();
            map.computeIfAbsent(consumerId, k -> new ArrayList<>()).add(categoryId);
        }

        for (var entry : map.entrySet()) {
            recommendationRedisService.putAll(entry.getKey(), entry.getValue());
        }
    }

    public RecommendationResponse getRecommendation(Jwt accessToken) {
        if (accessToken == null || accessToken.getClaims() == null) {
            return recommendationElasticService.getRecommendation();
        }

        return recommendationElasticService.getRecommendation(
                recommendationRedisService.getAllRelatedCategoryId(accessToken.getSubject())
        );
    }

    public List<Long> findSearchHistory(Jwt accessToken) {
        return recommendationRedisService.getAllCategoryId(accessToken.getSubject());
    }
}
