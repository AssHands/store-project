package com.ak.store.review.service;

import com.ak.store.common.model.consumer.form.CommentForm;
import com.ak.store.common.model.consumer.form.ReviewForm;
import com.ak.store.user.model.entity.Comment;
import com.ak.store.user.model.entity.Consumer;
import com.ak.store.user.model.entity.Review;
import com.ak.store.user.model.projection.ReviewWithCommentCountProjection;
import com.ak.store.user.repository.CommentRepo;
import com.ak.store.user.repository.ReviewRepo;
import com.ak.store.user.util.mapper.ReviewMapper;
import com.ak.store.user.validator.service.ReviewBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {


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

    public Review createOneReview(Long productId, String consumerId, ReviewForm reviewForm) {
        reviewBusinessValidator.validateCreation(productId, consumerId);
        Consumer consumer = consumerService.findOne(consumerId);
        Review review = reviewMapper.toReview(reviewForm, consumer, productId);
        return reviewRepo.save(review);
    }

    public Review updateOneReview(Long ReviewId, ReviewForm reviewForm) {
        Review review = findOneReview(ReviewId);
        updateReview(review, reviewForm);
        return reviewRepo.save(review);
    }

    public void deleteAllReviewByProductId(Long productId) {
        reviewRepo.deleteAllByProductId(productId);
    }

    public List<Comment> findAllCommentByReviewId(Long reviewId) {
        return commentRepo.findAllByReviewId(reviewId);
    }

    public Comment createOneComment(String consumerId, Long reviewId, CommentForm commentForm) {
        var commentReview = Comment.builder()
                .review(Review.builder().id(reviewId).build())
                .consumer(Consumer.builder().id(UUID.fromString(consumerId)).build())
                .text(commentForm.getText())
                .build();

        return commentRepo.save(commentReview);
    }

    public void deleteOneComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    public Comment updateOneComment(Long commentId, CommentForm commentForm) {
        Comment comment = findOmeComment(commentId);
        updateComment(comment, commentForm);
        return commentRepo.save(comment);
    }

    private void updateReview(Review review, ReviewForm reviewForm) {
        if (reviewForm.getAdvantages() != null) {
            review.setAdvantages(reviewForm.getAdvantages());
        }
        if (reviewForm.getDisadvantages() != null) {
            review.setDisadvantages(reviewForm.getDisadvantages());
        }
        if (reviewForm.getText() != null) {
            review.setText(reviewForm.getText());
        }
        if (reviewForm.getGrade() != null) {
            review.setGrade(reviewForm.getGrade());
        }
    }

    private void updateComment(Comment comment, CommentForm commentForm) {
        if (commentForm.getText() != null) {
            comment.setText(commentForm.getText());
        }
    }
}
