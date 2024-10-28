package com.ak.store.filterQuery;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        FilterQueryGenerator filterQueryGenerator =
                new FilterQueryGenerator("product_new", "leftEntry", "rightEntry");

        System.out.println(
                filterQueryGenerator.generateQueryWithFilters(Map.of("name", "2:43:2", "age", "30", "city", "12:s:7"))
        );
    }
}
