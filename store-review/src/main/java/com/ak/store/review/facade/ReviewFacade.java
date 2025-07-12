package com.ak.store.review.facade;

import com.ak.store.common.snapshot.review.ReviewCreationSnapshot;
import com.ak.store.common.snapshot.review.ReviewDeletionSnapshot;
import com.ak.store.common.snapshot.review.ReviewUpdateSnapshotPayload;
import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.dto.write.ReviewWriteDTO;
import com.ak.store.review.outbox.OutboxEventService;
import com.ak.store.review.outbox.OutboxEventType;
import com.ak.store.review.service.CommentService;
import com.ak.store.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final OutboxEventService outboxEventService;
    private final ReviewMapper reviewMapper;

    //todo add sorting
    //todo ДОБАВИТЬ, ЧТОБЫ ИСКАЛО ТОЛЬКО ОТЗЫВЫ С COMPLETED СТАТУСОМ
    public List<ReviewDTO> findAllByProductId(Long productId, int page, int size) {
        return reviewService.findAllByProductId(productId, page, size);
    }

    @Transactional
    public ReviewDTO createOne(UUID userId, ReviewWriteDTO request) {
        var review = reviewService.createOne(userId, request);

        var snapshot = ReviewCreationSnapshot.builder()
                .reviewId(review.getId().toString())
                .productId(review.getProductId())
                .grade(review.getGrade())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_CREATION);
        return review;
    }

    @Transactional
    public ReviewDTO updateOne(UUID userId, ObjectId reviewId, ReviewWriteDTO request) {
        //todo заменить на findAndUpdate
        var oldReview = reviewService.findOne(reviewId);
        var newReview = reviewService.updateOne(userId, reviewId, request);

        var snapshot = ReviewUpdateSnapshotPayload.builder()
                .review(reviewMapper.toReviewSnapshot(oldReview))
                .newGrade(newReview.getGrade())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_UPDATE);
        return newReview;
    }

    @Transactional
    public void deleteOne(UUID userId, ObjectId reviewId) {
        //todo добавить метод findAndDelete, чтобы уменьшить кол-во запросов
        var review = reviewService.findOne(reviewId);

        var snapshot = ReviewDeletionSnapshot.builder()
                .reviewId(review.getId().toString())
                .productId(review.getProductId())
                .grade(review.getGrade())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_DELETION);

        try {
            reviewService.deleteOne(userId, reviewId);
            commentService.deleteAllByReviewId(reviewId);
        } catch (Exception e) {
            //todo добавить компенсирующий метод для восстановления комментариев
            reviewService.compensateDeleteOne(review);
            throw new RuntimeException("cant delete");
        }
    }
}