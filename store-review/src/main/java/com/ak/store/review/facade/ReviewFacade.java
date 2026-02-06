package com.ak.store.review.facade;

import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.model.document.ReviewStatus;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;

    //todo ДОБАВИТЬ, ЧТОБЫ ИСКАЛО ТОЛЬКО ОТЗЫВЫ С COMPLETED СТАТУСОМ
    public List<ReviewDTO> findAllByProductId(Long productId) {
        return reviewService.findAllByProductId(productId);
    }

    @Transactional
    public ReviewDTO createOne(WriteReviewCommand command) {
        var review = reviewService.createOne(command);

//        var snapshot = ReviewCreationSnapshot.builder()
//                .reviewId(review.getId().toString())
//                .productId(review.getProductId())
//                .grade(review.getGrade())
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_CREATION);
        return review;
    }

    @Transactional
    public ReviewDTO updateOne(WriteReviewCommand command) {
        //todo добавить метод findAndUpdate, чтобы уменьшить кол-во запросов
        var oldReview = reviewService.findOne(command.getReviewId());
        var newReview = reviewService.updateOne(command);
//
//        var snapshot = ReviewUpdateSnapshotPayload.builder()
//                .review(reviewMapper.toSnapshot(oldReview))
//                .newGrade(newReview.getGrade())
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_UPDATE);
        return newReview;
    }

    @Transactional
    public void deleteOne(WriteReviewCommand command) {
        //todo добавить метод findAndUpdate, чтобы уменьшить кол-во запросов
        var review = reviewService.findOne(command.getReviewId());
        reviewService.updateOneStatus(command, ReviewStatus.IN_PROGRESS);
//
//        var snapshot = ReviewDeletionSnapshot.builder()
//                .reviewId(review.getId().toString())
//                .productId(review.getProductId())
//                .grade(review.getGrade())
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_DELETION);
    }
}