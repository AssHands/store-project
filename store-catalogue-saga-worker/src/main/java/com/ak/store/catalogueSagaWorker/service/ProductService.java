package com.ak.store.catalogueSagaWorker.service;

import com.ak.store.catalogueSagaWorker.model.entity.Product;
import com.ak.store.catalogueSagaWorker.model.entity.ProductStatus;
import com.ak.store.catalogueSagaWorker.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;

    private Product findOneById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void deleteOne(Long id) {
        productRepo.deleteById(id);
    }

    public void setStatus(Long id, ProductStatus status) {
        var product = findOneById(id);
        product.setStatus(status);
        productRepo.save(product);
    }
}
