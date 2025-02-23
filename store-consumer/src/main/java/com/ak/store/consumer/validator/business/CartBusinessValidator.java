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
        if(!catalogueFeign.availableOne(productId)) {
            throw new RuntimeException("product with id=%d is not exist".formatted(productId));
        }

        if(isProductExistInCart(consumerId, productId)) {
            throw new RuntimeException("product is already in cart");
        }
    }

    public void validateSetProductAmount(Long consumerId, Long productId) {
        if(!isProductExistInCart(consumerId, productId)) {
            throw new RuntimeException("product is not in cart");
        }
    }

    private boolean isProductExistInCart(Long consumerId, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(consumerId, productId).isPresent();
    }
}
