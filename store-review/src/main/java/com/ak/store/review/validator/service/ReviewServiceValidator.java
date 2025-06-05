package com.ak.store.review.validator.service;

import com.ak.store.review.feign.CatalogueFeign;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.dto.write.ReviewWriteDTO;
import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ReviewServiceValidator {
    private final ReviewRepo reviewRepo;
    private final CatalogueFeign catalogueFeign;

    public void validateCreating(UUID userId, ReviewWriteDTO request) {
        if (!isProductExist(request.getProductId())) {
            throw new RuntimeException("product not exist");
        }

        if(isUserHasReviewOnProduct(userId, request.getProductId())) {
            throw new RuntimeException("user already has a review on this product");
        }
    }

    public void validateUpdating(UUID userId, ObjectId reviewId) {
        if(!isReviewBelongToUser(userId, reviewId)) {
            throw new RuntimeException("review do not belong to user");
        }
    }

    public void validateDeleting(UUID userId, ObjectId reviewId) {
        if(!isReviewBelongToUser(userId, reviewId)) {
            throw new RuntimeException("review do not belong to user");
        }
    }

    private boolean isProductExist(Long productId) {
        return catalogueFeign.isExistAllProduct(List.of(productId));
    }

    private boolean isReviewBelongToUser(UUID userId, ObjectId reviewId) {
        var review = findOneById(reviewId);
        return review.getUserId().equals(userId);
    }

    private boolean isUserHasReviewOnProduct(UUID userId, Long productId) {
        var review = reviewRepo.findOneByUserIdAndProductId(userId, productId);
        return review.isPresent();
    }

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
