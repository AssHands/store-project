package com.ak.store.review.facade;

import com.ak.store.common.model.user.form.CommentForm;
import com.ak.store.common.model.user.form.ReviewForm;
import com.ak.store.common.model.user.view.CommentView;
import com.ak.store.common.model.user.view.ReviewView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    public List<ReviewView> findAllReviewByProductId(Long productId) {

    }

    public List<CommentView> findAllCommentByReviewId(Long ReviewId) {

    }

    public Long createOneReview(String consumerId, Long productId, ReviewForm reviewForm) {

    }

    public Long updateOneReview(Long reviewId, ReviewForm reviewForm) {
    }

    public void deleteOneReview(Long reviewId) {
    }

    public Long createOneComment(String consumerId, Long reviewId, CommentForm commentForm) {
    }

    public Long updateOneComment(Long commentId, CommentForm commentForm) {
    }

    public void deleteOneComment(Long commentId) {
    }

    public void deleteAllByProductId(Long productId) {
    }

}
