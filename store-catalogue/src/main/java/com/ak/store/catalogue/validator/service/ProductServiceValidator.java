package com.ak.store.catalogue.validator.service;

import com.ak.store.common.model.catalogue.form.ProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductServiceValidator {
    public void validateCreation(ProductForm productForm) {
        if (productForm.getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }
    }
}
