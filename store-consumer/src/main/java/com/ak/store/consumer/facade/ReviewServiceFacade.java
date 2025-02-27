package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.CommentReviewDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CommentReviewView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.service.ReviewService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewServiceFacade {
    private final ReviewService reviewService;
    private final ConsumerMapper consumerMapper;

    public List<ReviewView> findAllByProductId(Long productId) {
        return reviewService.findAllByProductId(productId).stream()
                .map(consumerMapper::mapToReviewView)
                .toList();
    }

    public List<CommentReviewView> findAllCommentByReviewId(Long ReviewId) {
        return reviewService.findAllCommentById(ReviewId).stream()
                .map(consumerMapper::mapToCommentReviewView)
                .toList();
    }

    @Transactional
    //todo: отправлять сообщение о новой оценки в redis для аналитики
    public Long createOne(Long productId, String consumerId, ReviewDTO reviewDTO) {
        Long reviewId = reviewService.createOne(productId, consumerId, reviewDTO).getId();

        return reviewId;
    }

    @Transactional
    //todo: отправлять сообщение о новой оценки в redis для аналитики
    public Long updateOne(Long productId, String consumerId, ReviewDTO reviewDTO) {
        Long reviewId = reviewService.updateOne(productId, consumerId, reviewDTO).getId();

        return reviewId;
    }

    @Transactional
    public Long createOneComment(String consumerId, Long reviewId, CommentReviewDTO commentReviewDTO) {
        return reviewService.createOneComment(consumerId, reviewId, commentReviewDTO).getId();
    }

    @Transactional
    //todo: отправлять сообщение о удалении оценки в redis для аналитики
    public void deleteOne(Long productId, String consumerId) {
        reviewService.deleteOne(productId, consumerId);
    }

    @Transactional
    public void deleteAllByProductId(Long productId) {
        reviewService.deleteAllByProductId(productId);
    }

}
