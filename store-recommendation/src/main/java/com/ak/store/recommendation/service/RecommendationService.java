package com.ak.store.recommendation.service;

import com.ak.store.recommendation.mapper.ProductMapper;
import com.ak.store.recommendation.model.dto.RecommendationDTO;
import com.ak.store.recommendation.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ProductRepo productRepo;
    private final SearchHistoryService searchHistoryService;
    private final ProductMapper productMapper;

    private final int RECOMMENDATION_SIZE = 20;

    public RecommendationDTO getRecommendation() {
        return new RecommendationDTO(
                productRepo.findRandom(RECOMMENDATION_SIZE).stream()
                .map(productMapper::toDTO)
                .toList()
        );
    }

    public RecommendationDTO getRecommendation(UUID userId) {
        var searchHistory = searchHistoryService.getOneByUserId(userId);

        return new RecommendationDTO(
                productRepo.findRandomByCategoryIds(searchHistory, RECOMMENDATION_SIZE).stream()
                        .map(productMapper::toDTO)
                        .toList()
        );
    }
}
