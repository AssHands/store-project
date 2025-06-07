package com.ak.store.review.facade;

import com.ak.store.common.snapshot.review.ReviewUpdatedSnapshotPayload;
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
    public List<ReviewDTO> findAllByProductId(Long productId, int page, int size) {
        return reviewService.findAllByProductId(productId, page, size);
    }

    @Transactional
    public ReviewDTO createOne(UUID userId, ReviewWriteDTO request) {
        var review = reviewService.createOne(userId, request);
        outboxEventService.createOne(reviewMapper.toReviewSnapshot(review), OutboxEventType.REVIEW_CREATED);
        return review;
    }

    @Transactional
    public ReviewDTO updateOne(UUID userId, ObjectId reviewId, ReviewWriteDTO request) {
        //todo заменить на findAndUpdate
        var oldReview = reviewService.findOne(reviewId);
        var review = reviewService.updateOne(userId, reviewId, request);

        var snapshot = ReviewUpdatedSnapshotPayload.builder()
                .review(reviewMapper.toReviewSnapshot(review))
                .oldGrade(oldReview.getGrade())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.REVIEW_UPDATED);
        return review;
    }

    @Transactional
    public void deleteOne(UUID userId, ObjectId reviewId) {
        //todo добавить метод findAndDelete, чтобы уменьшить кол-во запросов
        var review = reviewService.findOne(reviewId);
        outboxEventService.createOne(reviewMapper.toReviewDeletedSnapshot(review), OutboxEventType.REVIEW_DELETED);

        try {
            reviewService.deleteOne(userId, reviewId);
            commentService.deleteAllByReviewId(reviewId);
        } catch (Exception e) {
            //todo добавить компенсирующий метод для восстановления самого комментария
            reviewService.compensateDeleteOne(review);
            throw new RuntimeException("cant delete");
        }
    }
}