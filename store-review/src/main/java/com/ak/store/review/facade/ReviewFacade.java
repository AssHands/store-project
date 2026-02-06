package com.ak.store.review.facade;

import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.model.document.ReviewStatus;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.service.ReviewOutboxService;
import com.ak.store.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final ReviewOutboxService reviewOutboxService;

    public List<ReviewDTO> findAllByProductId(Long productId) {
        return reviewService.findAllByProductId(productId);
    }

    @Transactional
    public ObjectId createOne(WriteReviewCommand command) {
        var review = reviewService.createOne(command);
        reviewOutboxService.saveCreatedEvent(review.getId());
        return review.getId();
    }

    @Transactional
    public ObjectId updateOne(WriteReviewCommand command) {
        var review = reviewService.updateOne(command);
        reviewOutboxService.saveUpdatedEvent(review.getId());
        return review.getId();
    }

    @Transactional
    public void deleteOne(WriteReviewCommand command) {
        reviewService.updateOneStatus(command, ReviewStatus.IN_PROGRESS);
        reviewOutboxService.saveCreatedEvent(command.getReviewId());
    }
}