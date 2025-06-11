package com.ak.store.cart.validator.service;

import com.ak.store.cart.feign.CatalogueFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartServiceValidator {
    private final CatalogueFeign catalogueFeign;

    public void validateAdding(Long productId) {
        if(!isProductExist(productId)) {
            throw new RuntimeException("product does not exist");
        }
    }

    private boolean isProductExist(Long productId) {
        return catalogueFeign.isExistAllProduct(List.of(productId));
    }
}
