package com.ak.store.review.validator;

import com.ak.store.review.feign.CatalogueFeign;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ReviewValidator {
    private final ReviewRepo reviewRepo;
    private final CatalogueFeign catalogueFeign;

    public void validateCreate(WriteReviewCommand command) {
        productExist(command.getProductId());
        userNotHaveReviewOnProduct(command.getUserId(), command.getProductId());
    }

    public void validateUpdate(WriteReviewCommand command) {
        reviewBelongToUser(command.getUserId(), command.getReviewId());
    }

    private void productExist(Long productId) {
        if(!catalogueFeign.isExistAllProduct(List.of(productId))) {
            throw new RuntimeException("product not exist");
        }
    }

    private void reviewBelongToUser(UUID userId, ObjectId reviewId) {
        var review = findOneById(reviewId);

        if(review.getUserId().equals(userId)) {
            throw new RuntimeException("review do not belong to user");
        }
    }

    private void userNotHaveReviewOnProduct(UUID userId, Long productId) {
        var review = reviewRepo.findOneByUserIdAndProductId(userId, productId);

        if(review.isPresent()) {
            throw new RuntimeException("user already has a review on this product");
        }
    }

    private Review findOneById(ObjectId reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
