package com.ak.store.queryGenerator;

import java.util.Map;

public class QueryGenerator {

    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public QueryGenerator(String tableName, String leftEntry, String rightEntry) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
    }

    public String generateQuery() {
        return "SELECT * FROM " + tableName;
    }

    public String generateQuery(String sort) {
        StringBuilder query = new StringBuilder(generateQuery());
        query.append(generateSortCondition(sort));
        return query.toString();
    }

    public String generateQuery(String sort, int limit, int offset) {
        StringBuilder query = new StringBuilder(generateQuery());
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(limit, offset));

        return query.toString();
    }

    public String generateQuery(String sort, int limit, int offset,
                                Map<String, String> filters) {
        StringBuilder query = new StringBuilder(generateQuery());
        query.append(FilterQueryGenerator.generateQuery(leftEntry, rightEntry, filters));
        query.append(generateSortCondition(sort));
        query.append(generatePaginationCondition(limit, offset));

        return query.toString();
    }

    private String generateSortCondition(String sort) {
        StringBuilder condition = new StringBuilder(" ORDER BY ");

        switch (sort) {
            case "popular" :
                condition.append("amount_orders"); break; //TODO: need new table
            case "priceup" :
                condition.append("price"); break;
            case "pricedown" :
                condition.append("price ASC"); break;
            case "sale" :
                condition.append("sale"); break;
            case "rate" :
                condition.append("rate"); break;
            case "newly" :
                condition.append("created_at ASC"); break;
            default:
                condition.append("amount_orders"); break;
        }

        return condition.toString();
    }

    private String generatePaginationCondition(int limit, int offset) {
        return " LIMIT " + limit + " OFFSET " + offset;
    }
}