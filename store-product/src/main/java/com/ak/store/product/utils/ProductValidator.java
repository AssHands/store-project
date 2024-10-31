package com.ak.store.product.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductValidator {
    public boolean validate(String sort) {
        List<String> allowsSortParams = List.of("amount_orders", "price ASC", "price DESC", "sale", "rate", "created_at ASC");
        return allowsSortParams.contains(sort);
    }
}
