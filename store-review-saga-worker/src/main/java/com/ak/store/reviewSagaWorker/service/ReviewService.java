package com.ak.store.reviewSagaWorker.service;

import com.ak.store.reviewSagaWorker.mapper.ReviewMapper;
import com.ak.store.reviewSagaWorker.model.command.WriteReviewCommand;
import com.ak.store.reviewSagaWorker.model.document.Review;
import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
import com.ak.store.reviewSagaWorker.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void updateOneStatus(ObjectId id, ReviewStatus status) {
        reviewRepo.updateOneStatusById(id, status);
    }

    public void cancelOneCreated(ObjectId id) {
        reviewRepo.deleteById(id);
    }

    public void deleteOne(ObjectId id) {
        reviewRepo.deleteById(id);
    }

    public void cancelOneUpdate(ObjectId id, WriteReviewCommand command) {
        var review = findOneById(id);
        reviewMapper.updateEntity(command, review);

        review.setStatus(ReviewStatus.COMPLETED);
        reviewRepo.save(review);
    }
}