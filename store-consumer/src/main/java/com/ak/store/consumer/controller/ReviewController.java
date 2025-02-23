package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.dto.CommentReviewDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CommentReviewView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.facade.ReviewServiceFacade;
import com.ak.store.consumer.model.entity.CommentReview;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/consumer/reviews")
public class ReviewController {
    private final ReviewServiceFacade reviewServiceFacade;

    @GetMapping("{productId}")
    public List<ReviewView> findAll(@PathVariable Long productId) {
        return reviewServiceFacade.findAllByProductId(productId);
    }

    @GetMapping("{id}/comments")
    public List<CommentReviewView> findAllComment(@PathVariable Long id) {
        return reviewServiceFacade.findAllCommentById(id);
    }

    @PostMapping("{id}/comments")
    public Long createOneComment(@PathVariable Long id, @RequestParam Long consumerId,
                                 @RequestBody @Valid CommentReviewDTO commentReviewDTO) {
        return reviewServiceFacade.createOneComment(consumerId, id, commentReviewDTO);
    }

    @PostMapping("{productId}")
    public Long createOne(@PathVariable Long productId, @RequestParam Long consumerId,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewServiceFacade.createOne(productId, consumerId, reviewDTO);
    }

    @PatchMapping("{productId}")
    public Long updateOne(@PathVariable Long productId, @RequestParam Long consumerId,
                          @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewServiceFacade.updateOne(productId, consumerId, reviewDTO);
    }

    @DeleteMapping("{productId}")
    public void deleteOne(@PathVariable Long productId, @RequestParam Long consumerId) {
        reviewServiceFacade.deleteOne(productId, consumerId);
    }
}
