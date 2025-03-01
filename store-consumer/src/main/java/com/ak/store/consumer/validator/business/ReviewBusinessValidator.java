package com.ak.store.consumer.validator.business;

import com.ak.store.consumer.feign.CatalogueFeign;
import com.ak.store.consumer.repository.ReviewRepo;
import com.ak.store.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.glassfish.jaxb.core.v2.TODO;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewBusinessValidator {
    private final CatalogueFeign catalogueFeign;

    private final ConsumerService consumerService;

    private final ReviewRepo reviewRepo;

    public void validateCreation(Long productId, String consumerId) {
        if(!catalogueFeign.existOne(productId)) {
            throw new RuntimeException("product with id=%d is not exists".formatted(productId));
        }

        boolean isExist = reviewRepo.findOneByProductIdAndConsumerId(productId, consumerId).isPresent();
        if(isExist) {
            throw new RuntimeException("this customer already has review on this product");
        }
    }
}
