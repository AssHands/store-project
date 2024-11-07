package com.ak.store.product.utils;

import com.ak.store.common.dto.ProductFullDTO;
import org.apache.commons.text.CaseUtils;
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
        if(filters == null) {
            return true;
        }

        for (var entry : filters.entrySet()) {
            if(!(isValidKey(entry.getKey()) && isValidValue(entry.getValue()))) {
                return false;
            }
        }

        return true;
    }

    public boolean validateSortAndFilters(String sort, Map<String, String> filters) {
        return validateSort(sort) && validateFilters(filters);
    }

    public boolean validateUpdatedFields(Map<String, ? super Object> updatedFields)  {
        String fieldName = "";

        try {
            for (var entry : updatedFields.entrySet()) {
                fieldName = CaseUtils.toCamelCase(entry.getKey(), false, '_');
                Class<?> type = ProductFullDTO.class.getDeclaredField(fieldName).getType();

                if(Number.class.isAssignableFrom(entry.getValue().getClass())
                && Number.class.isAssignableFrom(type)) {
                    continue;
                }

                if(String.class.isAssignableFrom(entry.getValue().getClass())
                && String.class.isAssignableFrom(type)) {
                    continue;
                }

                if(Map.class.isAssignableFrom(entry.getValue().getClass())
                && Map.class.isAssignableFrom(type)) {
                    continue;
                }

                throw new RuntimeException("Invalid type for field " + fieldName);
            }

            return true;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unknown filed " + fieldName);
        }
    }

    private boolean isValidValue(String value) {
         return Arrays.stream(value.split(":"))
                .allMatch(str -> str.matches("^\\d+$"));
    }

    private boolean isValidKey(String key) {
        if (key.matches("^[a-z_]+$")
                && key.length() <= 15
                && !key.isBlank()) {
            return true;
        }

        return false;
    }
}