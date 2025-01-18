package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductId(Long productId);

    void deleteAllByProductId(Long productId);
}
