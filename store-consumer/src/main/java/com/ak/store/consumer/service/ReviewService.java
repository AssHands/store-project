package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.CommentReviewDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.consumer.model.entity.CommentReview;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.Review;
import com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection;
import com.ak.store.consumer.repository.CommentReviewRepo;
import com.ak.store.consumer.repository.ReviewRepo;
import com.ak.store.consumer.util.ConsumerMapper;
import com.ak.store.consumer.validator.business.ReviewBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final CommentReviewRepo commentReviewRepo;
    private final ReviewBusinessValidator reviewBusinessValidator;
    private final ConsumerMapper consumerMapper;

    public List<ReviewWithCommentCountProjection> findAllByProductId(Long productId) {
        return reviewRepo.findAllWithConsumerAndCommentCountByProductId(productId);
    }

    public Review findOneWithComments(Long productId, Long consumerId) {
        return reviewRepo.findOneWithCommentsByProductIdAndConsumerId(productId, consumerId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public Review findOne(Long productId, Long consumerId) {
        return reviewRepo.findOneByProductIdAndConsumerId(productId, consumerId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public void deleteOne(Long productId, Long consumerId) {
        reviewRepo.delete(findOne(productId, consumerId));
    }

    public Review createOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        reviewBusinessValidator.validateCreation(productId, consumerId);
        Review review = consumerMapper.mapToReview(reviewDTO, productId, consumerId);
        review.setAmountDislikes(0);
        review.setAmountLikes(0);
        return reviewRepo.save(review);
    }

    public Review updateOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        Review review = findOne(productId, consumerId);
        updateReview(review, reviewDTO);
        return reviewRepo.save(review);
    }

    private void updateReview(Review review, ReviewDTO reviewDTO) {
        if (reviewDTO.getAdvantages() != null) {
            review.setAdvantages(reviewDTO.getAdvantages());
        }
        if (reviewDTO.getDisadvantages() != null) {
            review.setDisadvantages(reviewDTO.getDisadvantages());
        }
        if (reviewDTO.getText() != null) {
            review.setText(reviewDTO.getText());
        }
        if (reviewDTO.getGrade() != null) {
            review.setGrade(reviewDTO.getGrade());
        }
    }

    public void deleteAllByProductId(Long productId) {
        reviewRepo.deleteAllByProductId(productId);
    }

    public List<CommentReview> findAllCommentById(Long id) {
        return commentReviewRepo.findAllByReviewId(id);
    }

    public CommentReview createOneComment(Long consumerId, Long reviewId, CommentReviewDTO commentReviewDTO) {
        var commentReview = CommentReview.builder()
                .review(Review.builder().id(reviewId).build())
                .consumer(Consumer.builder().id(consumerId).build())
                .text(commentReviewDTO.getText())
                .amountDislikes(0)
                .amountLikes(0)
                .build();

        return commentReviewRepo.save(commentReview);
    }
}
