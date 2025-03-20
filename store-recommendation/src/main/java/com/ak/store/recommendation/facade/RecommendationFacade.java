package com.ak.store.recommendation.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.common.model.recommendation.RecommendationResponse;
import com.ak.store.recommendation.service.RecommendationElasticService;
import com.ak.store.recommendation.service.SearchHistoryRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RecommendationFacade {
    private final SearchHistoryRedisService searchHistoryRedisService;
    private final RecommendationElasticService recommendationElasticService;

    public void putInSearchHistory(List<SearchAllEvent> searchAllEvents) {
        Map<String, List<Long>> map = new HashMap<>(searchAllEvents.size());

        for(var searchEvent: searchAllEvents) {
            String consumerId = searchEvent.getConsumerSearch().getConsumerId();
            Long categoryId = searchEvent.getConsumerSearch().getCategoryId();
            map.computeIfAbsent(consumerId, k -> new ArrayList<>()).add(categoryId);
        }

        for(var entry : map.entrySet()) {
            searchHistoryRedisService.putAll(entry.getKey(), entry.getValue());
        }
    }

    public RecommendationResponse getRecommendation(Jwt accessToken) {
        var set = searchHistoryRedisService.getAllCategoryId(accessToken.getSubject());
        return recommendationElasticService.getRecommendation(set);
    }

    public Set<Long> findSearchHistory(Jwt accessToken) {
        return searchHistoryRedisService.getAllCategoryId(accessToken.getSubject());
    }
}
