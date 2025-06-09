package com.ak.store.catalogueRatingUpdater.facade;

import com.ak.store.catalogueRatingUpdater.mapper.ProductMapper;
import com.ak.store.catalogueRatingUpdater.outbox.OutboxEventService;
import com.ak.store.catalogueRatingUpdater.outbox.OutboxEventType;
import com.ak.store.catalogueRatingUpdater.service.RatingUpdaterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RatingUpdaterFacade {
    private final RatingUpdaterService ratingUpdaterService;
    private final OutboxEventService outboxEventService;
    private final ProductMapper productMapper;

    @Transactional
    public void createOne(Long productId, Integer grade) {
        var productRating = ratingUpdaterService.createOne(productId, grade);
        outboxEventService.createOne(productMapper.toProductRatingUpdatedSnapshot(productRating),
                OutboxEventType.PRODUCT_RATING_UPDATED);
    }

    @Transactional
    public void updateOne(Long productId, Integer newGrade, Integer oldGrade) {
        var productRating = ratingUpdaterService.updateOne(productId, newGrade, oldGrade);
        outboxEventService.createOne(productMapper.toProductRatingUpdatedSnapshot(productRating),
                OutboxEventType.PRODUCT_RATING_UPDATED);
    }

    @Transactional
    public void deleteOne(Long productId, Integer grade) {
        var productRating = ratingUpdaterService.deleteOne(productId, grade);
        outboxEventService.createOne(productMapper.toProductRatingUpdatedSnapshot(productRating),
                OutboxEventType.PRODUCT_RATING_UPDATED);
    }
}