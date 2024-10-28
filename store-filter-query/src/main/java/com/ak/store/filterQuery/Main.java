package com.ak.store.filterQuery;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String str = "12:18";

        System.out.println(
                FilterQueryGenerator.getValidFilters(str)
        );

        System.out.println(
                FilterQueryGenerator.generateQuery("{\"color\": 1, \"size\": 129, \"sounds\": 16}")
        );
    }
}
