package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.CommentDTO;
import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.consumer.model.entity.Comment;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.Review;
import com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection;
import com.ak.store.consumer.repository.CommentRepo;
import com.ak.store.consumer.repository.ReviewRepo;
import com.ak.store.consumer.util.ConsumerMapper;
import com.ak.store.consumer.validator.business.ReviewBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final CommentRepo commentRepo;
    private final ReviewBusinessValidator reviewBusinessValidator;
    private final ConsumerMapper consumerMapper;

    public List<ReviewWithCommentCountProjection> findAllByProductId(Long productId) {
        return reviewRepo.findAllWithConsumerAndCommentCountByProductId(productId);
    }

    public Review findOneReviewWithComments(Long productId, String consumerId) {
        return reviewRepo.findOneWithCommentsByProductIdAndConsumerId(productId, consumerId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public Review findOneReview(Long productId, String consumerId) {
        return reviewRepo.findOneByProductIdAndConsumerId(productId, UUID.fromString(consumerId))
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public Comment findOmeComment(Long commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("no comment found"));
    }

    private Review findOneReview(Long reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public void deleteOneReview(Long reviewId) {
        reviewRepo.deleteById(reviewId);
    }

    public Review createOneReview(Long productId, String consumerId, ReviewDTO reviewDTO) {
        reviewBusinessValidator.validateCreation(productId, consumerId);
        Review review = consumerMapper.mapToReview(reviewDTO, productId, consumerId);
        return reviewRepo.save(review);
    }

    public Review updateOneReview(Long ReviewId, ReviewDTO reviewDTO) {
        Review review = findOneReview(ReviewId);
        updateReview(review, reviewDTO);
        return reviewRepo.save(review);
    }

    public void deleteAllReviewByProductId(Long productId) {
        reviewRepo.deleteAllByProductId(productId);
    }

    public List<Comment> findAllCommentByReviewId(Long reviewId) {
        return commentRepo.findAllByReviewId(reviewId);
    }

    public Comment createOneComment(String consumerId, Long reviewId, CommentDTO commentDTO) {
        var commentReview = Comment.builder()
                .review(Review.builder().id(reviewId).build())
                .consumer(Consumer.builder().id(UUID.fromString(consumerId)).build())
                .text(commentDTO.getText())
                .build();

        return commentRepo.save(commentReview);
    }

    public void deleteOneComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    public Comment updateOneComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = findOmeComment(commentId);
        updateComment(comment, commentDTO);
        return commentRepo.save(comment);
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

    private void updateComment(Comment comment, CommentDTO commentDTO) {
        if (commentDTO.getText() != null) {
            comment.setText(commentDTO.getText());
        }
    }
}
