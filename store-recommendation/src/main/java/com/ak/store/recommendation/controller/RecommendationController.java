package com.ak.store.recommendation.controller;

import com.ak.store.recommendation.mapper.RecommendationMapper;
import com.ak.store.recommendation.model.view.RecommendationView;
import com.ak.store.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final RecommendationMapper recommendationMapper;

    @GetMapping
    public RecommendationView getRecommendation(@AuthenticationPrincipal Jwt accessToken) {
        return recommendationMapper.toView(
                accessToken == null ?
                        recommendationService.getRecommendation() :
                        recommendationService.getRecommendation(UUID.fromString(accessToken.getSubject()))
        );
    }
}