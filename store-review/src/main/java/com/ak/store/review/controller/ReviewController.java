package com.ak.store.review.controller;

import com.ak.store.review.facade.ReviewFacade;
import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.form.ReviewForm;
import com.ak.store.review.model.view.ReviewView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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

    //todo добавить удаление всех отзывов при удалении продукта (кафка)
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
        return review.getId().toString();
    }

    @PatchMapping("{reviewId}")
    public String updateOne(@AuthenticationPrincipal Jwt accessToken,
                            @PathVariable ObjectId reviewId, @RequestBody @Valid ReviewForm request) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var review = reviewFacade.updateOne(userId, reviewId, reviewMapper.toReviewWriteDTO(request));
        return review.getId().toString();
    }

    @DeleteMapping("{reviewId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable ObjectId reviewId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        reviewFacade.deleteOne(userId, reviewId);
    }
}