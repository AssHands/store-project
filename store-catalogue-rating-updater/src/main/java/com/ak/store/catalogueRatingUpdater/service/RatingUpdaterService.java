package com.ak.store.catalogueRatingUpdater.service;

import com.ak.store.catalogueRatingUpdater.mapper.ProductMapper;
import com.ak.store.catalogueRatingUpdater.model.dto.ProductRatingDTO;
import com.ak.store.catalogueRatingUpdater.model.entity.Product;
import com.ak.store.catalogueRatingUpdater.repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ak.store.catalogueRatingUpdater.service.RatingSummaryCalculator.*;

@RequiredArgsConstructor
@Service
public class RatingUpdaterService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    private Product findOneById(Long productId) {
        return productRepo.findOneWithRatingSummaryById(productId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    @Transactional
    public ProductRatingDTO createOne(Long productId, Integer grade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        calculateCreating(ratingSummary, grade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        return productMapper.toProductRatingDTO(productRepo.save(product));
    }

    @Transactional
    public ProductRatingDTO updateOne(Long productId, Integer newGrade, Integer oldGrade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        calculateUpdating(ratingSummary, newGrade, oldGrade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        return productMapper.toProductRatingDTO(productRepo.save(product));
    }

    @Transactional
    public ProductRatingDTO deleteOne(Long productId, Integer grade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        calculateDeleting(ratingSummary, grade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        return productMapper.toProductRatingDTO(productRepo.save(product));
    }
}