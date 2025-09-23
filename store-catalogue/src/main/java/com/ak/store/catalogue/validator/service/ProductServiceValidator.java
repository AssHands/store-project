package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductServiceValidator {
    public void validateCreating(ProductWriteDTO product) {
        if (product.getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }
    }
}
