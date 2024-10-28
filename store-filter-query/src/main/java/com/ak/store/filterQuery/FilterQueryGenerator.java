package com.ak.store.filterQuery;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class FilterQueryGenerator {
    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public FilterQueryGenerator(String tableName, String leftEntry, String rightEntry) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
    }

    public String generateQueryWithFilters(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
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

    private String getValidValue(String value) {
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

    private String getValidKey(String key) {
        if (key.matches("^[a-zA-Z_]+$")
                && key.length() <= 15
                && !key.isBlank()) {
            return key;
        } else {
            throw new IllegalArgumentException("incorrect column format");
        }
    }
}