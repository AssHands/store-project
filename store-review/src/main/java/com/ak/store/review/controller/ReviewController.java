package com.ak.store.review.controller;

import com.ak.store.review.facade.ReviewFacade;
import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.form.ReviewForm;
import com.ak.store.review.model.view.ReviewView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/review/reviews")
public class ReviewController {
    private final ReviewFacade reviewFacade;
    private final ReviewMapper reviewMapper;

    //todo добавить сортировку
    @GetMapping("product/{productId}")
    public List<ReviewView> findAllByProductId(@PathVariable Long productId,
                                               @RequestParam int page, @RequestParam int size) {
        return reviewMapper.toReviewView(reviewFacade.findAllByProductId(productId, page, size));
    }

    @PostMapping
    public String createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody @Valid ReviewForm request) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var review = reviewFacade.createOne(userId, reviewMapper.toReviewWriteDTO(request));
        return review.getId();
    }

    @PatchMapping("{reviewId}")
    public String updateOne(@AuthenticationPrincipal Jwt accessToken,
                            @PathVariable String reviewId, @RequestBody @Valid ReviewForm request) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var review = reviewFacade.updateOne(userId, reviewId, reviewMapper.toReviewWriteDTO(request));
        return review.getId();
    }

    @DeleteMapping("{reviewId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable String reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reviewFacade.deleteOne(userId, reviewId);
    }

//    @GetMapping("{reviewId}/comments")
//    public List<CommentView> findAllCommentByReviewId(@PathVariable Long reviewId) {
//        reviewFacade.findAllCommentByReviewId(reviewId);
//        return null;
//    }


//    @PostMapping("{reviewId}/comments")
//    public Long createOneComment(@PathVariable Long reviewId, @RequestParam String consumerId,
//                                 @RequestBody @Valid CommentForm commentForm) {
//        reviewFacade.createOneComment(consumerId, reviewId, commentForm);
//        return null;
//    }
//
//    @PatchMapping("comments/{commentId}")
//    public Long updateOneComment(@PathVariable Long commentId, @RequestBody @Valid CommentForm commentForm) {
//        reviewFacade.updateOneComment(commentId, commentForm);
//        return null;
//    }
//
//    @DeleteMapping("comments/{commentId}")
//    public void deleteOneComment(@PathVariable Long commentId) {
//        reviewFacade.deleteOneComment(commentId);
//    }


//    @PostMapping("me/{reviewId}/comments")
//    public Long createOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long reviewId,
//                                   @RequestBody @Valid CommentForm commentForm) {
//        reviewFacade.createOneComment(accessToken.getSubject(), reviewId, commentForm);
//        return null;
//    }
//
//    @PatchMapping("me/comments/{commentId}")
//    public Long updateOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long commentId,
//                                   @RequestBody @Valid CommentForm commentForm) {
//        reviewFacade.updateOneComment(commentId, commentForm);
//        return null;
//    }
//
//    @DeleteMapping("me/comments/{commentId}")
//    public void deleteOneCommentMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long commentId) {
//        reviewFacade.deleteOneComment(commentId);
//    }
}