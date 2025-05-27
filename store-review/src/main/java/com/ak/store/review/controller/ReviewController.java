package com.ak.store.review.controller;

import com.ak.store.common.model.user.form.CommentForm;
import com.ak.store.common.model.user.form.ReviewForm;
import com.ak.store.common.model.user.view.CommentView;
import com.ak.store.common.model.user.view.ReviewView;

import com.ak.store.review.facade.ReviewFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/consumer/reviews")
public class ReviewController {
    private final ReviewFacade reviewFacade;

    @GetMapping("{productId}")
    public List<ReviewView> findAllReview(@PathVariable Long productId) {
        return reviewFacade.findAllReviewByProductId(productId);
    }

    @GetMapping("{reviewId}/comments")
    public List<CommentView> findAllCommentByReviewId(@PathVariable Long reviewId) {
        return reviewFacade.findAllCommentByReviewId(reviewId);
    }

    @PostMapping("{productId}")
    public Long createOneReview(@PathVariable Long productId, @RequestParam String consumerId,
                                @RequestBody @Valid ReviewForm reviewForm) {
        return reviewFacade.createOneReview(consumerId, productId, reviewForm);
    }

    @PatchMapping("{reviewId}")
    public Long updateOneReview(@PathVariable Long reviewId, @RequestBody @Valid ReviewForm reviewForm) {
        return reviewFacade.updateOneReview(reviewId, reviewForm);
    }

    @DeleteMapping("{reviewId}")
    public void deleteOneReview(@PathVariable Long reviewId) {
        reviewFacade.deleteOneReview(reviewId);
    }

    @PostMapping("{reviewId}/comments")
    public Long createOneComment(@PathVariable Long reviewId, @RequestParam String consumerId,
                                 @RequestBody @Valid CommentForm commentForm) {
        return reviewFacade.createOneComment(consumerId, reviewId, commentForm);
    }

    @PatchMapping("comments/{commentId}")
    public Long updateOneComment(@PathVariable Long commentId, @RequestBody @Valid CommentForm commentForm) {
        return reviewFacade.updateOneComment(commentId, commentForm);
    }

    @DeleteMapping("comments/{commentId}")
    public void deleteOneComment(@PathVariable Long commentId) {
        reviewFacade.deleteOneComment(commentId);
    }

    //----------------

    @PostMapping("me/{productId}")
    public Long createOneReviewMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId,
                                  @RequestBody @Valid ReviewForm reviewForm) {
        return reviewFacade.createOneReview(accessToken.getSubject(), productId, reviewForm);
    }

    @PatchMapping("me/{reviewId}")
    public Long updateOneReviewMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long reviewId,
                                  @RequestBody @Valid ReviewForm reviewForm) {
        return reviewFacade.updateOneReview(reviewId, reviewForm);
    }

    @DeleteMapping("me/{reviewId}")
    public void deleteOneReviewMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long reviewId) {
        reviewFacade.deleteOneReview(reviewId);
    }

    @PostMapping("me/{reviewId}/comments")
    public Long createOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long reviewId,
                                   @RequestBody @Valid CommentForm commentForm) {
        return reviewFacade.createOneComment(accessToken.getSubject(), reviewId, commentForm);
    }

    @PatchMapping("me/comments/{commentId}")
    public Long updateOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long commentId,
                                   @RequestBody @Valid CommentForm commentForm) {
        return reviewFacade.updateOneComment(commentId, commentForm);
    }

    @DeleteMapping("me/comments/{commentId}")
    public void deleteOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long commentId) {
        reviewFacade.deleteOneComment(commentId);
    }
}
