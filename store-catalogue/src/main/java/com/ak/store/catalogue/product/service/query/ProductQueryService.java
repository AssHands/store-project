package com.ak.store.catalogue.product.service.query;

import com.ak.store.catalogue.exception.NotFoundException;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepo productRepo;

    public Product findByIdOrThrow(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: id=" + productId));
    }

    public List<Product> findAllByIds(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    public void assertExists(Long productId) {
        findByIdOrThrow(productId);
    }

    public Product findOneWithCategoryCharacteristicsOrThrow(Long productId) {
        return productRepo.findOneWithCategoryCharacteristicsById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: id=" + productId));
    }

    public Product findOneWithCharacteristicsOrThrow(Long productId) {
        return productRepo.findOneWithCharacteristicsById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: id=" + productId));
    }
}
