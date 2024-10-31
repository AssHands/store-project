package com.ak.store.queryGenerator;

import java.util.Arrays;
import java.util.stream.Collectors;

class QueryFilterValidator {
    static String getValidValue(String value) {
    String result = Arrays.stream(value.split(":"))
            .filter(str -> str.matches("^\\d+$"))
            .distinct()
            .collect(Collectors.joining(", "));

    if(!result.isBlank()) {
        return result;
    } else {
        throw new IllegalArgumentException("incorrect value format");
    }
}

    static String getValidKey(String key) {
        if (key.matches("^[a-zA-Z_]+$")
                && key.length() <= 15
                && !key.isBlank()) {
            return key;
        } else {
            throw new IllegalArgumentException("incorrect column format");
        }
    }
}