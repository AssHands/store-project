//package com.ak.store.user.facade;
//
//import com.ak.store.common.model.consumer.form.CommentForm;
//import com.ak.store.common.model.consumer.form.ReviewForm;
//import com.ak.store.common.model.consumer.view.CommentView;
//import com.ak.store.common.model.consumer.view.ReviewView;
//import com.ak.store.user.service.ReviewService;
//import com.ak.store.user.util.mapper.ReviewMapper;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class ReviewFacade {
//    private final ReviewService reviewService;
//    private final ReviewMapper reviewMapper;
//
//    public List<ReviewView> findAllReviewByProductId(Long productId) {
//        return reviewService.findAllByProductId(productId).stream()
//                .map(reviewMapper::toReviewView)
//                .toList();
//    }
//
//    public List<CommentView> findAllCommentByReviewId(Long ReviewId) {
//        return reviewService.findAllCommentByReviewId(ReviewId).stream()
//                .map(reviewMapper::toCommentView)
//                .toList();
//    }
//
//    @Transactional
//    public Long createOneReview(String consumerId, Long productId, ReviewForm reviewForm) {
//        var a = reviewService.createOneReview(productId, consumerId, reviewForm).getId();
//        return a;
//    }
//
//    @Transactional
//    public Long updateOneReview(Long reviewId, ReviewForm reviewForm) {
//        return reviewService.updateOneReview(reviewId, reviewForm).getId();
//    }
//
//    @Transactional
//    public void deleteOneReview(Long reviewId) {
//        reviewService.deleteOneReview(reviewId);
//    }
//
//    @Transactional
//    public Long createOneComment(String consumerId, Long reviewId, CommentForm commentForm) {
//        return reviewService.createOneComment(consumerId, reviewId, commentForm).getId();
//    }
//
//    @Transactional
//    public Long updateOneComment(Long commentId, CommentForm commentForm) {
//        return reviewService.updateOneComment(commentId, commentForm).getId();
//    }
//
//    @Transactional
//    public void deleteOneComment(Long commentId) {
//        reviewService.deleteOneComment(commentId);
//    }
//
//
//    @Transactional
//    public void deleteAllByProductId(Long productId) {
//        reviewService.deleteAllReviewByProductId(productId);
//    }
//
//}
