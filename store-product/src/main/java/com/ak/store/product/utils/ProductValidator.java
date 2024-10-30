package com.ak.store.product.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductValidator {
    public boolean validate(String sort) {
        List<String> allowsSortParams = List.of("popular", "priceup", "pricedown", "sale", "rate", "newly");
        return allowsSortParams.contains(sort);
    }
}
