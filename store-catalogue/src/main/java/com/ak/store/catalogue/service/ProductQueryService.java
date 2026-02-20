package com.ak.store.catalogue.service;

import com.ak.store.catalogue.exception.NotFoundException;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepo productRepo;

    public void assertExists(Long productId) {
        productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: id=" + productId));
    }

    public Product findOneWithCategoryCharacteristicsOrThrow(Long productId) {
        return productRepo.findOneWithCategoryCharacteristicsById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: id=" + productId));
    }
}
