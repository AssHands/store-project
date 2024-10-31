package com.ak.store.product.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ProductValidator {

    List<String> allowsSortParams;

    public ProductValidator() {
        allowsSortParams = new ArrayList<>(List.of("amount_orders", "price ASC", "price DESC", "sale", "rate", "created_at ASC"));
    }

    public boolean validateSort(String sort) {
        return allowsSortParams.contains(sort);
    }
    public boolean validateFilters(Map<String, String> filters) {
        for (var entry : filters.entrySet()) {
            if(!isValidKey(entry.getKey()) && !isValidValue(entry.getValue())) {
                return false;
            }
        }

        return true;
    }

    public boolean validateSortAndFilters(String sort, Map<String, String> filters) {
        return validateSort(sort) && validateFilters(filters);
    }

    private boolean isValidValue(String value) {
         return Arrays.stream(value.split(":"))
                .allMatch(str -> str.matches("^\\d+$"));
    }

    private boolean isValidKey(String key) {
        if (key.matches("^[a-zA-Z_]+$")
                && key.length() <= 15
                && !key.isBlank()) {
            return true;
        }

        return false;
    }
}