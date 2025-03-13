package com.ak.store.catalogue.validator.business;

import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.common.model.catalogue.form.ProductForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductBusinessValidator {
    private final CategoryService categoryService;

    public void validateCreation(ProductForm productForm) {
        if (productForm.getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }

        //todo: здесь лучше сервис или репозиторий?
        //check for category exists
        categoryService.findOne(productForm.getCategoryId());
    }
}
