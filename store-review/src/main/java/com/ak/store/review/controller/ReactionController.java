package com.ak.store.review.controller;

import com.ak.store.review.facade.ReactionFacade;
import com.ak.store.review.mapper.ReactionMapper;
import com.ak.store.review.model.view.ReactionView;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/review/reactions")
public class ReactionController {
    private final ReactionFacade reactionFacade;
    private final ReactionMapper reactionMapper;

    @PostMapping("like")
    public void likeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam ObjectId reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.likeOneReview(userId, reviewId);
    }

    @PostMapping("dislike")
    public void dislikeOneReview(@AuthenticationPrincipal Jwt accessToken, @RequestParam ObjectId reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.dislikeOneReview(userId, reviewId);
    }

    @DeleteMapping("remove")
    public void removeOne(@AuthenticationPrincipal Jwt accessToken, @RequestParam ObjectId reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reactionFacade.removeOne(userId, reviewId);
    }

    @PostMapping("find")
    public List<ReactionView> findAllByReviewIds(@AuthenticationPrincipal Jwt accessToken,
                                                 @RequestBody List<ObjectId> reviewIds) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return reactionMapper.toReactionView(reactionFacade.findAllByReviewIds(userId, reviewIds));
    }
}