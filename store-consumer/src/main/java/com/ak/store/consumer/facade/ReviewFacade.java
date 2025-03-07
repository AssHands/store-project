package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.CommentDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CommentView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.service.ReviewService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final ConsumerMapper consumerMapper;

    public List<ReviewView> findAllReviewByProductId(Long productId) {
        return reviewService.findAllByProductId(productId).stream()
                .map(consumerMapper::mapToReviewView)
                .toList();
    }

    public List<CommentView> findAllCommentByReviewId(Long ReviewId) {
        return reviewService.findAllCommentByReviewId(ReviewId).stream()
                .map(consumerMapper::mapToCommentReviewView)
                .toList();
    }

    @Transactional
    public Long createOneReview(String consumerId, Long productId, ReviewDTO reviewDTO) {
        var a = reviewService.createOneReview(productId, consumerId, reviewDTO).getId();
        return a;
    }

    @Transactional
    public Long updateOneReview(Long reviewId, ReviewDTO reviewDTO) {
        return reviewService.updateOneReview(reviewId, reviewDTO).getId();
    }

    @Transactional
    public void deleteOneReview(Long reviewId) {
        reviewService.deleteOneReview(reviewId);
    }

    @Transactional
    public Long createOneComment(String consumerId, Long reviewId, CommentDTO commentDTO) {
        return reviewService.createOneComment(consumerId, reviewId, commentDTO).getId();
    }

    @Transactional
    public Long updateOneComment(Long commentId, CommentDTO commentDTO) {
        return reviewService.updateOneComment(commentId, commentDTO).getId();
    }

    @Transactional
    public void deleteOneComment(Long commentId) {
        reviewService.deleteOneComment(commentId);
    }


    @Transactional
    public void deleteAllByProductId(Long productId) {
        reviewService.deleteAllReviewByProductId(productId);
    }

}
