package com.ak.store.catalogueSagaWorker.service;


import com.ak.store.catalogueSagaWorker.model.entity.Product;
import com.ak.store.catalogueSagaWorker.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RatingUpdaterService {
    private final ProductRepo productRepo;
    private final RatingSummaryCalculator ratingCalculator;

    private Product findOneById(Long productId) {
        return productRepo.findOneWithRatingSummaryById(productId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void createOne(Long productId, Integer grade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        ratingCalculator.calculateCreating(ratingSummary, grade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        productRepo.save(product);
    }

    public void updateOne(Long productId, Integer newGrade, Integer oldGrade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        ratingCalculator.calculateUpdating(ratingSummary, newGrade, oldGrade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        productRepo.save(product);
    }

    public void deleteOne(Long productId, Integer grade) {
        var product = findOneById(productId);
        var ratingSummary = product.getRatingSummary();

        ratingCalculator.calculateDeleting(ratingSummary, grade);
        product.setRating(ratingSummary.getAverage());
        product.setReviewAmount(ratingSummary.getAmount());

        productRepo.save(product);
    }
}