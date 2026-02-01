package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.command.WriteProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductServiceValidator {
    public void validateCreate(WriteProductCommand product) {
        if (product.getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }
    }
}
