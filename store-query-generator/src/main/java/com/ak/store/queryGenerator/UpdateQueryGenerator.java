package com.ak.store.queryGenerator;

import java.util.Map;

public class UpdateQueryGenerator<T> {
    private final String tableName;
    private final String leftEntry;
    private final String rightEntry;

    public UpdateQueryGenerator(String tableName, String leftEntry, String rightEntry) {
        this.tableName = tableName;
        this.leftEntry = leftEntry;
        this.rightEntry = rightEntry;
    }

    public String update(T id, Class<?> clazz, Map<String, ?> updatedFields) {

        return null;
    }

    private String generateUpdateCondition() {
        return "UPDATE " + tableName;
    }

    private String generateSetCondition(Class<?> clazz, Map<String, ?> updatedFields) {
        StringBuilder query = new StringBuilder(" SET ");

        //updatedFields.
        return null;
    }

}
