package com.ak.store.review.service;

import com.ak.store.review.mapper.ReviewMapper;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.document.ReviewStatus;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.repository.ReviewRepo;
import com.ak.store.review.validator.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewMapper reviewMapper;
    private final ReviewValidator reviewValidator;

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public ReviewDTO findOne(ObjectId reviewId) {
        return reviewMapper.toDTO(findOneById(reviewId));
    }

    public List<ReviewDTO> findAllByProductId(Long productId) {
        return reviewRepo.findAllByProductIdAndStatus(productId, ReviewStatus.COMPLETED).stream()
                .map(reviewMapper::toDTO)
                .toList();
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

    public void updateReactionCounter(ObjectId reviewId, int likeDelta, int dislikeDelta)  {
        reviewRepo.updateReactionCounter(reviewId, likeDelta, dislikeDelta);
    }

    public void updateCommentCounter(ObjectId reviewId, int commentDelta)  {
        reviewRepo.updateCommentCounter(reviewId, commentDelta);
    }
}