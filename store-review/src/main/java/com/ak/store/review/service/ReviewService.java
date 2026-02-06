package com.ak.store.review.service;

import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.document.ReviewStatus;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.repository.ReviewRepo;
import com.ak.store.review.validator.service.ReviewServiceValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;
    private final ReviewServiceValidator reviewValidator;

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public ReviewDTO findOne(ObjectId reviewId) {
        return reviewMapper.toDTO(findOneById(reviewId));
    }

    public List<ReviewDTO> findAllByProductId(Long productId) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewMapper.toDTO(
                reviewRepo.findAllByProductIdAndStatus(productId, ReviewStatus.COMPLETED, pageable)
        );
    }

    public ReviewDTO createOne(WriteReviewCommand command) {
        reviewValidator.validateCreate(command);
        var review = reviewMapper.toDocument(command);

        review.setStatus(ReviewStatus.IN_PROGRESS);
        review.setTime(LocalDateTime.now());
        review.setLikeAmount(0);
        review.setDislikeAmount(0);
        review.setCommentAmount(0);

        return reviewMapper.toDTO(reviewRepo.save(review));
    }

    public ReviewDTO updateOne(WriteReviewCommand command) {
        reviewValidator.validateUpdate(command);

        var review = findOneById(command.getReviewId());
        reviewMapper.updateDocument(command, review);
        review.setStatus(ReviewStatus.IN_PROGRESS);

        return reviewMapper.toDTO(reviewRepo.save(review));
    }

    public void updateOneStatus(WriteReviewCommand command, ReviewStatus status) {
        reviewValidator.validateUpdate(command);

        var review = findOneById(command.getReviewId());
        review.setStatus(status);

        reviewRepo.save(review);
    }

    public void updateCommentCounter(ObjectId reviewId) {

    }

    public void updateReactionCounter(ObjectId reviewId, boolean isLike, boolean isIncrement, boolean needTo)  {

    }

    public void incrementCommentAmount(ObjectId reviewId) {
        reviewRepo.incrementCommentAmount(reviewId);
    }

    public void decrementCommentAmount(ObjectId reviewId) {
        reviewRepo.decrementCommentAmount(reviewId);
    }

    public void incrementLikeAmount(ObjectId reviewId) {
        reviewRepo.incrementLikeAmount(reviewId);
    }

    public void decrementLikeAmount(ObjectId reviewId) {
        reviewRepo.decrementLikeAmount(reviewId);
    }

    public void incrementDislikeAmount(ObjectId reviewId) {
        reviewRepo.incrementDislikeAmount(reviewId);
    }

    public void decrementDislikeAmount(ObjectId reviewId) {
        reviewRepo.decrementDislikeAmount(reviewId);
    }

    public void incrementLikeAmountAndDecrementDislikeAmount(ObjectId reviewId) {
        reviewRepo.incrementLikeAmountAndDecrementDislikeAmount(reviewId);
    }

    public void decrementLikeAmountAndIncrementDislikeAmount(ObjectId reviewId) {
        reviewRepo.decrementLikeAmountAndIncrementDislikeAmount(reviewId);
    }
}