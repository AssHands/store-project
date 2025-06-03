package com.ak.store.review.service;

import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.dto.write.ReviewWriteDTO;
import com.ak.store.review.repository.ReviewRepo;
import com.ak.store.review.validator.service.ReviewServiceValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;
    private final ReviewServiceValidator reviewValidator;

    private Review findOneById(String reviewId) {
        return reviewRepo.findById(new ObjectId(reviewId))
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public ReviewDTO findOne(String reviewId) {
        return reviewMapper.toReviewDTO(findOneById(reviewId));
    }

    public List<ReviewDTO> findAllByProductId(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewMapper.toReviewDTO(reviewRepo.findAllByProductId(productId, pageable));
    }

    public ReviewDTO createOne(UUID userId, ReviewWriteDTO request) {
        reviewValidator.validateCreating(userId, request);
        var review = reviewMapper.toReview(request);

        review.setTime(LocalDateTime.now());
        review.setUserId(userId);
        review.setLikeAmount(0);
        review.setDislikeAmount(0);
        review.setCommentAmount(0);

        return reviewMapper.toReviewDTO(reviewRepo.save(review));
    }

    public ReviewDTO updateOne(UUID userId, String reviewId, ReviewWriteDTO request) {
        reviewValidator.validateUpdating(userId, reviewId);
        var review = findOneById(reviewId);

        updateOneFromDTO(review, request);

        return reviewMapper.toReviewDTO(reviewRepo.save(review));
    }

    public void deleteOne(UUID userId, String reviewId) {
        reviewValidator.validateDeleting(userId, reviewId);
        reviewRepo.deleteById(new ObjectId(reviewId));
    }

    private void updateOneFromDTO(Review review, ReviewWriteDTO request) {
        if(request.getText() != null) {
            review.setText(request.getText());
        }

        if(request.getAdvantages() != null) {
            review.setAdvantages(request.getAdvantages());
        }

        if(request.getDisadvantages() != null) {
            review.setDisadvantages(request.getDisadvantages());
        }

        if(request.getGrade() != null) {
            review.setGrade(request.getGrade());
        }
    }

    public void compensateDeleteOne(ReviewDTO request) {
        var review = reviewMapper.toReview(request);
        reviewRepo.save(review);
    }

    public void incrementOneCommentAmount(String reviewId) {
        reviewRepo.incrementOneCommentAmount(reviewId);
    }

    public void decrementOneCommentAmount(String reviewId) {
        reviewRepo.decrementOneCommentAmount(reviewId);
    }

    public void incrementOneLikeAmount(String reviewId) {
        reviewRepo.incrementOneLikeAmount(reviewId);
    }

    public void incrementOneDislikeAmount(String reviewId) {
        reviewRepo.incrementOneISLikeAmount(reviewId);
    }
}