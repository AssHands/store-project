package com.ak.store.review.controller;

import com.ak.store.review.facade.ReviewFacade;
import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.command.WriteReviewCommand;
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
    @GetMapping("find/{productId}")
    public List<ReviewView> findAllByProductId(@PathVariable Long productId) {
        return reviewFacade.findAllByProductId(productId).stream()
                .map(reviewMapper::toView)
                .toList();
    }

    @PostMapping
    public String createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody @Valid ReviewForm form) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return reviewFacade.createOne(reviewMapper.toWriteCommand(userId, form)).toString();
    }

    @PatchMapping("update")
    public String updateOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody @Valid ReviewForm form) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return reviewFacade.updateOne(reviewMapper.toWriteCommand(userId, form)).toString();
    }

    @DeleteMapping("{reviewId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable ObjectId reviewId) {
        var command = WriteReviewCommand.builder()
                .userId(UUID.fromString(accessToken.getSubject()))
                .reviewId(reviewId)
                .build();

        reviewFacade.deleteOne(command);
    }
}