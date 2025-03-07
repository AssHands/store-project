package com.ak.store.consumer.validator.business;

import com.ak.store.consumer.feign.CatalogueFeign;
import com.ak.store.consumer.repository.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CartBusinessValidator {
    private final CatalogueFeign catalogueFeign;
    private final CartRepo cartRepo;

    public void validateCreation(String consumerId, Long productId) {
        if(!catalogueFeign.availableOne(productId)) {
            throw new RuntimeException("product with id=%d is not available".formatted(productId));
        }

        if(isProductExistInCart(consumerId, productId)) {
            throw new RuntimeException("product is already in cart");
        }
    }

    public void validateSetProductAmount(String consumerId, Long productId) {
        if(!isProductExistInCart(consumerId, productId)) {
            throw new RuntimeException("product is not in cart");
        }
    }

    private boolean isProductExistInCart(String consumerId, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(UUID.fromString(consumerId), productId).isPresent();
    }
}
