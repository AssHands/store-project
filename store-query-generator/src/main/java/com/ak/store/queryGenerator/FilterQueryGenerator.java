package com.ak.store.queryGenerator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class FilterQueryGenerator {
    public static String generateQuery(String leftEntry, String rightEntry,
                                       Map<String, String> filters) {
        StringBuilder sql = new StringBuilder(" WHERE ");
        boolean firstCondition = true;

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (!firstCondition)
                sql.append(" AND ");

            sql.append(leftEntry)
                    .append("'" + getValidKey(entry.getKey()) + "'")
                    .append(rightEntry)
                    .append(" IN (")
                    .append(getValidValue(entry.getValue()))
                    .append(")");

            firstCondition = false;
        }

        return sql.toString();
    }

    private static String getValidValue(String value) {
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

    private static String getValidKey(String key) {
        if (key.matches("^[a-zA-Z_]+$")
                && key.length() <= 15
                && !key.isBlank()) {
            return key;
        } else {
            throw new IllegalArgumentException("incorrect column format");
        }
    }
}