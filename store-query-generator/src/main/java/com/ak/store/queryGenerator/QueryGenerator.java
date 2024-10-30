package com.ak.store.queryGenerator;

import java.lang.reflect.Field;
import java.util.Map;

public class QueryGenerator <T> {
    private Class<T> clazz;
    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public QueryGenerator(String tableName, String leftEntry, String rightEntry, Class<T> clazz) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
        this.clazz = clazz;
    }

    public QueryGenerator(String tableName) {
        this.tableName = tableName;
        this.leftEntry = "";
        this.rightEntry = "";
    }

    public String generateQuery() {
        return "SELECT * FROM " + tableName;
    }

    public String generateQuery(String sort) {
        StringBuilder query = new StringBuilder(generateQuery());
        query.append(generateSortCondition(sort));
        return query.toString();
    }

    public String generateQuery(String sort, int offset, int limit) {

        Field[] fields = clazz.getDeclaredFields();
        for (var f : fields) {
            System.out.println(f.getName());
        }

        StringBuilder query = new StringBuilder(generateQuery());
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));
        return query.toString();
    }

    public String generateQuery(String sort, int offset, int limit,
                                Map<String, String> filters) {
        StringBuilder query = new StringBuilder(generateQuery());
        query.append(FilterConditionGenerator.generateFilterCondition(leftEntry, rightEntry, filters));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(offset, limit));
        return query.toString();
    }

    private String generateSortCondition(String sort) {
        StringBuilder condition = new StringBuilder(" ORDER BY ");

        switch (sort) {
            case "popular" -> condition.append("amount_orders"); //TODO: need solution
            case "priceup" -> condition.append("price");
            case "pricedown" -> condition.append("price DESC");
            case "sale" -> condition.append("sale");
            case "rate" -> condition.append("rate");
            case "newly" -> condition.append("created_at ASC");
            default -> condition.append("amount_orders");
        }

        return condition.toString();
    }

    private String generatePaginationCondition(int offset, int limit) {
        return " LIMIT " + limit + " OFFSET " + offset;
    }
}