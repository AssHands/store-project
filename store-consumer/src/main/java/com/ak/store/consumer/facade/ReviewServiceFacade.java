package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.service.ReviewService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewServiceFacade {
    private final ReviewService reviewService;
    private final ConsumerMapper consumerMapper;

    public List<ReviewView> findAllByProductId(Long productId) {
        return reviewService.findAllByProductId(productId).stream()
                .map(consumerMapper::mapToReviewView)
                .toList();
    }

    @Transactional
    public Long createOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        return reviewService.createOne(productId, consumerId, reviewDTO).getId();
    }

    @Transactional
    public Long updateOne(Long productId, Long consumerId, ReviewDTO reviewDTO) {
        return reviewService.updateOne(productId, consumerId, reviewDTO).getId();
    }

    @Transactional
    public void deleteOne(Long productId, Long consumerId) {
        reviewService.deleteOne(productId, consumerId);
    }
}
