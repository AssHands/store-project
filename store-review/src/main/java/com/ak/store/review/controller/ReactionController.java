package com.ak.store.review.controller;

import com.ak.store.review.facade.ReactionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/review/reactions")
public class ReactionController {
    private final ReactionFacade reactionFacade;

    @PostMapping("like")
    public void likeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam String reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.likeOneReview(userId, reviewId);
    }

    @PostMapping("unlike")
    public void unlikeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam String reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.unlikeOneReview(userId, reviewId);
    }

    @PostMapping("dislike")
    public void dislikeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam String reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.dislikeOneReview(userId, reviewId);
    }

    @PostMapping("undislike")
    public void undislikeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam String reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.undislikeOneReview(userId, reviewId);
    }
}