package com.ak.store.consumer.validator.business;

import com.ak.store.consumer.feign.CatalogueFeign;
import com.ak.store.consumer.repository.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CartBusinessValidator {
    private final CatalogueFeign catalogueFeign;
    private final CartRepo cartRepo;

    public void validateCreation(Long consumerId, Long productId) {
        catalogueFeign.existOne(productId);

        if(isProductExist(consumerId, productId)) {
            throw new RuntimeException("product is already in cart");
        }
    }

    public void validateSetAmountProducts(Long consumerId, Long productId) {
        if(!isProductExist(consumerId, productId)) {
            throw new RuntimeException("product is not in cart");
        }
    }

    private boolean isProductExist(Long consumerId, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(consumerId, productId).isPresent();
    }
}
