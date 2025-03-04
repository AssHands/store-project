package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.dto.CommentReviewDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CommentReviewView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.facade.ReviewFacade;
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
    public List<ReviewView> findAll(@PathVariable Long productId) {
        return reviewFacade.findAllByProductId(productId);
    }

    @GetMapping("{reviewId}/comments")
    public List<CommentReviewView> findAllCommentByReviewId(@PathVariable Long reviewId) {
        return reviewFacade.findAllCommentByReviewId(reviewId);
    }

    @PostMapping("{productId}")
    public Long createOne(@PathVariable Long productId, @RequestParam String consumerId,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewFacade.createOne(productId, consumerId, reviewDTO);
    }

    @PatchMapping("{productId}")
    public Long updateOne(@PathVariable Long productId, @RequestParam String consumerId,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewFacade.updateOne(productId, consumerId, reviewDTO);
    }

    @DeleteMapping("{productId}")
    public void deleteOne(@PathVariable Long productId, @RequestParam String consumerId) {
        reviewFacade.deleteOne(productId, consumerId);
    }

    @PostMapping("{reviewId}/comments")
    public Long createOneComment(@PathVariable Long reviewId, @RequestParam String consumerId,
                                 @RequestBody @Valid CommentReviewDTO commentReviewDTO) {
        return reviewFacade.createOneComment(consumerId, reviewId, commentReviewDTO);
    }

    //----------------

    @PostMapping("me/{productId}")
    public Long createOne(@PathVariable Long productId, @AuthenticationPrincipal Jwt accessToken,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewFacade.createOne(productId, accessToken.getSubject(), reviewDTO);
    }

    @PatchMapping("me/{productId}")
    public Long updateOne(@PathVariable Long productId, @AuthenticationPrincipal Jwt accessToken,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewFacade.updateOne(productId, accessToken.getSubject(), reviewDTO);
    }

    @DeleteMapping("me/{productId}")
    public void deleteOne(@PathVariable Long productId, @AuthenticationPrincipal Jwt accessToken) {
        reviewFacade.deleteOne(productId, accessToken.getSubject());
    }

    @PostMapping("me/{reviewId}/comments")
    public Long createOneComment(@PathVariable Long reviewId, @AuthenticationPrincipal Jwt accessToken,
                                 @RequestBody @Valid CommentReviewDTO commentReviewDTO) {
        return reviewFacade.createOneComment(accessToken.getSubject(), reviewId, commentReviewDTO);
    }
}
