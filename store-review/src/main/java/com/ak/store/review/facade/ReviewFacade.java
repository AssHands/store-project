package com.ak.store.review.facade;

import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.dto.write.ReviewWriteDTO;
import com.ak.store.review.service.CommentService;
import com.ak.store.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final CommentService commentService;

    //todo add sorting
    public List<ReviewDTO> findAllByProductId(Long productId, int page, int size) {
        return reviewService.findAllByProductId(productId, page, size);
    }

    public ReviewDTO createOne(UUID userId, ReviewWriteDTO request) {
        return reviewService.createOne(userId, request);
    }

    public ReviewDTO updateOne(UUID userId, String reviewId, ReviewWriteDTO request) {
        return reviewService.updateOne(userId, reviewId, request);
    }

    public void deleteOne(UUID userId, String reviewId) {
        var review = reviewService.findOne(reviewId);

        try {
            reviewService.deleteOne(userId, reviewId);
            commentService.deleteAllByReviewId(reviewId);
        } catch (Exception e) {
            reviewService.compensateDeleteOne(review);
            throw new RuntimeException("cant delete");
        }
    }
}