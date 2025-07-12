package com.ak.store.reviewSagaWorker.service;

import com.ak.store.reviewSagaWorker.model.document.Review;
import com.ak.store.reviewSagaWorker.model.document.ReviewStatus;
import com.ak.store.reviewSagaWorker.model.dto.write.ReviewWriteDTO;
import com.ak.store.reviewSagaWorker.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void updateOneStatus(ObjectId id, ReviewStatus status) {
        reviewRepo.updateOneStatusById(id, status);
    }

    public void cancelOneCreation(ObjectId id) {
        reviewRepo.deleteById(id);
    }

    public void cancelOneUpdate(ObjectId id, ReviewWriteDTO request) {
        var review = findOneById(id);
        updateOneFromDTO(review, request);
        review.setStatus(ReviewStatus.COMPLETED);
        reviewRepo.save(review);
    }

    public void deleteOne(ObjectId id) {
        reviewRepo.deleteById(id);
    }

    private void updateOneFromDTO(Review review, ReviewWriteDTO request) {
        if (request.getText() != null) {
            review.setText(request.getText());
        }

        if (request.getAdvantages() != null) {
            review.setAdvantages(request.getAdvantages());
        }

        if (request.getDisadvantages() != null) {
            review.setDisadvantages(request.getDisadvantages());
        }

        if (request.getGrade() != null) {
            review.setGrade(request.getGrade());
        }
    }
}