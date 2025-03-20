package com.ak.store.recommendation.controller;

import com.ak.store.common.model.recommendation.RecommendationResponse;
import com.ak.store.recommendation.facade.RecommendationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recommendation")
public class RecommendationController {
    private final RecommendationFacade recommendationFacade;

    @GetMapping
    public RecommendationResponse getRecommendation(@AuthenticationPrincipal Jwt accessToken) {
        return recommendationFacade.getRecommendation(accessToken);
    }
}
