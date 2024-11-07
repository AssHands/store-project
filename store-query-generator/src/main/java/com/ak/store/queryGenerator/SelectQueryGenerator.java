package com.ak.store.queryGenerator;

import org.springframework.data.util.ParsingUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectQueryGenerator {
    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public SelectQueryGenerator(String tableName, String leftEntry, String rightEntry) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
    }

    public String select(Class<?> clazz) {
        return generateSelectCondition(clazz);
    }

    public String select(String sort, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateSortCondition(sort);
    } 

    public String select(String sort, int offset, int limit, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateSortCondition(sort) +
                generatePaginationCondition(offset, limit);
    }

    public String select(String sort, int offset, int limit,
                         Map<String, String> filters, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateFilterCondition(filters) +
                generateSortCondition(sort) +
                generatePaginationCondition(offset, limit);
    }

    public String select(Map<String, String> filters, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateFilterCondition(filters);
    }

    public String select(int offset, int limit, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generatePaginationCondition(offset, limit);
    }

    private String generateSortCondition(String sort) {
        return " ORDER BY " + sort;
    }

    private String generatePaginationCondition(int offset, int limit) {
        return " LIMIT " + limit + " OFFSET " + offset;
    }

    private String generateSelectCondition(Class<?> clazz) {
        return "SELECT " + Arrays.stream(clazz.getDeclaredFields())
                .map(field -> ParsingUtils.reconcatenateCamelCase(field.getName(), "_"))
                .collect(Collectors.joining(", ")) + " FROM " + tableName;
    }

    private String generateFilterCondition(Map<String, String> filters) {
        StringBuilder query = new StringBuilder(" WHERE ");
        boolean firstCondition = true;

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (!firstCondition)
                query.append(" AND ");

            query.append(leftEntry)
                    .append(entry.getKey())
                    .append(rightEntry)
                    .append(" IN (")
                    .append(getSplitValue(entry.getValue()))
                    .append(")");

            firstCondition = false;
        }
        return query.toString();
    }

    private String getSplitValue(String value) {
        return Arrays.stream(value.split(":"))
                .distinct()
                .collect(Collectors.joining(", "));
    }
}