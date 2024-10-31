package com.ak.store.queryGenerator;

import org.springframework.data.util.ParsingUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryGenerator {
    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public QueryGenerator(String tableName, String leftEntry, String rightEntry) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
    }

    public String generateQuery(Class<?> clazz) {
        return generateSelectCondition(clazz) + " FROM " + tableName;
    }

    public String generateQuery(String sort, Class<?> clazz) {
        StringBuilder query = new StringBuilder(generateQuery(clazz));
        query.append(generateSortCondition(sort));
        return query.toString();
    }

    public String generateQuery(String sort, int offset, int limit, Class<?> clazz) {
        StringBuilder query = new StringBuilder(generateQuery(clazz));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));
        return query.toString();
    }

    public String generateQuery(String sort, int offset, int limit,
                                Map<String, String> filters, Class<?> clazz) {
        StringBuilder query = new StringBuilder(generateQuery(clazz));
        query.append(FilterConditionGenerator.generateFilterCondition(leftEntry, rightEntry, filters));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));
        return query.toString();
    }

    public String generateQuery(Map<String, String> filters, Class<?> clazz) {

        return null;
    }

    public String generateQuery(int offset, int limit) {

        return null;
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
                .collect(Collectors.joining(", "));
    }
}