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

    public String update(T id, Map<String, ? super Object> updatedFields) {
        StringBuilder query = new StringBuilder();

        query.append(generateUpdateCondition());
        query.append(generateSetCondition(updatedFields));
        query.append(generateWhereCondition(id));

        return query.toString();
    }

    private String generateUpdateCondition() {
        return "UPDATE " + tableName;
    }

    private String generateSetCondition(Map<String, ? super Object> updatedFields) {
        StringBuilder query = new StringBuilder(" SET ");

        boolean firstCondition = true;
        for(var entry : updatedFields.entrySet()) {

            if (!firstCondition)
                query.append(", ");

            query.append(entry.getKey());
            query.append(" = ");

            boolean isNum = !Number.class.isAssignableFrom(entry.getValue().getClass());
            if(!isNum) {
                query.append(entry.getValue());
            } else {
                query.append("'" + entry.getValue() + "'");
            }

            firstCondition = false;
        }

        return query.toString();
    }

    private String generateWhereCondition(T id) {
        StringBuilder query = new StringBuilder(" WHERE id = "); //todo: make id name template
        if(Number.class.isAssignableFrom(id.getClass())) {
            query.append(id);
        } else {
            query.append("'" + id + "'");
        }

        return query.toString();
    }
}
