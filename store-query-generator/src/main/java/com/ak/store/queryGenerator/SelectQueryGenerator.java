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
        StringBuilder query = new StringBuilder();

        query.append(generateSelectCondition(clazz));
        query.append(generateSortCondition(sort));

        return query.toString();
    }

    public String select(String sort, int offset, int limit, Class<?> clazz) {
        StringBuilder query = new StringBuilder();

        query.append(generateSelectCondition(clazz));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));

        return query.toString();
    }

    public String select(String sort, int offset, int limit,
                         Map<String, String> filters, Class<?> clazz) {
        StringBuilder query = new StringBuilder();

        query.append(generateSelectCondition(clazz));
        query.append(generateFilterCondition(filters));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));

        return query.toString();
    }

    public String select(Map<String, String> filters, Class<?> clazz) {
        StringBuilder query = new StringBuilder();

        query.append(generateSelectCondition(clazz));
        query.append(generateFilterCondition(filters));

        return query.toString();
    }

    public String select(int offset, int limit, Class<?> clazz) {
        StringBuilder query = new StringBuilder();

        query.append(generateSelectCondition(clazz));
        query.append(generatePaginationCondition(offset, limit));

        return query.toString();
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
        StringBuilder sql = new StringBuilder(" WHERE ");
        boolean firstCondition = true;

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (!firstCondition)
                sql.append(" AND ");

            sql.append(leftEntry)
                    .append(QueryFilterValidator.getValidKey(entry.getKey()))
                    .append(rightEntry)
                    .append(" IN (")
                    .append(QueryFilterValidator.getValidValue(entry.getValue()))
                    .append(")");

            firstCondition = false;
        }

        return sql.toString();
    }
}