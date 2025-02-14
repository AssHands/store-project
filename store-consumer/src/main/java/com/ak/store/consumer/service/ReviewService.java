package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.consumer.model.Review;
import com.ak.store.consumer.repository.ReviewRepo;
import com.ak.store.consumer.util.ConsumerMapper;
import com.ak.store.consumer.validator.business.ReviewBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ReviewBusinessValidator reviewBusinessValidator;
    private final ConsumerMapper consumerMapper;

    public List<Review> findAllByProductId(Long productId) {
        return reviewRepo.findAllWithConsumerAndCommentsByProductId(productId);
    }

    public Review findOneWithComments(Long productId, Long consumerId) {
        return reviewRepo.findOneWithCommentsByProductIdAndConsumerId(productId, consumerId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public Review findOne(Long productId, Long consumerId) {
        return reviewRepo.findOneByProductIdAndConsumerId(productId, consumerId)
                .orElseThrow(() -> new RuntimeException("no review found"));
    }

    public void deleteOne(Long productId, Long consumerId) {
        reviewRepo.delete(findOne(productId, consumerId));
    }

    public Review createOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        reviewBusinessValidator.validateCreation(productId, consumerId);
        Review review = consumerMapper.mapToReview(reviewDTO, productId, consumerId);
        review.setAmountDislikes(0);
        review.setAmountLikes(0);
        return reviewRepo.save(review);
    }

    public Review updateOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        Review review = findOne(productId, consumerId);
        updateReview(review, reviewDTO);
        return reviewRepo.save(review);
    }

    private void updateReview(Review review, ReviewDTO reviewDTO) {
        if(reviewDTO.getAdvantages() != null) {
            review.setAdvantages(reviewDTO.getAdvantages());
        }
        if(reviewDTO.getDisadvantages() != null) {
            review.setDisadvantages(reviewDTO.getDisadvantages());
        }
        if(reviewDTO.getText() != null) {
            review.setText(reviewDTO.getText());
        }
        if(reviewDTO.getGrade() != null) {
            review.setGrade(reviewDTO.getGrade());
        }
    }
}
