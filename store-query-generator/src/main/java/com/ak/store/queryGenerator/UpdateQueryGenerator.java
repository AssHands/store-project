package com.ak.store.queryGenerator;

import org.springframework.data.util.ParsingUtils;
import java.lang.reflect.Method;
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

    public <O> String update(T id, O updatedObject) {
        StringBuilder query = new StringBuilder();

        query.append(generateUpdateCondition())
                .append(generateSetCondition(updatedObject))
                .append(generateWhereCondition(id));

        return query.toString();
    }

    private String generateUpdateCondition() {
        return "UPDATE " + tableName;
    }



    private String generateWhereCondition(T id) {
        String query =" WHERE id = "; //todo: make id name template
        if(Number.class.isAssignableFrom(id.getClass())) {
            query += id;
        } else {
            query += "'" + id + "'";
        }

        return query;
    }

    private <O> String generateSetCondition(O updatedObject) {
        StringBuilder query = new StringBuilder(" SET ");
        Class<?> clazz = updatedObject.getClass();
        var fields = updatedObject.getClass().getDeclaredFields();
        boolean isFirstCondition = true;

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String rowName = ParsingUtils.reconcatenateCamelCase(fieldName, "_");
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object result;

            try {
                Method method = clazz.getMethod(methodName);
                result = method.invoke(updatedObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(result == null) {
                continue;
            }

            if(!isFirstCondition)
                query.append(", ");
            query.append(rowName); //todo: need to delete if map.getValue() == null
            query.append(" = ");

            if(Number.class.isAssignableFrom(fields[i].getType())) {
                query.append(result);

            } else if (Map.class.isAssignableFrom(fields[i].getType())) {
                Map<String, ? super Object> json = (Map<String, ? super Object>) result;
                boolean isFirstEntry = true;
                query.append(leftEntry);

                for(var entry : json.entrySet()) {
                    if(!isFirstEntry)
                        query.append(", ");

                    query.append("'" + entry.getKey() + "', ");

                    if(Number.class.isAssignableFrom(entry.getValue().getClass())) {
                        query.append(entry.getValue());
                    } else {
                        query.append("'" + entry.getValue() + "'");
                    }

                    isFirstEntry = false;
                }
                query.append(rightEntry);
            } else {
                query.append("'"+ result + "'");
            }

            isFirstCondition = false;
        }

        return query.toString();
    }
}
