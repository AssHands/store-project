package com.ak.store.queryGenerator;

import org.springframework.data.util.ParsingUtils;
import java.lang.reflect.Method;
import java.util.Map;

public class UpdateQueryGenerator {
    private final String tableName;

    public UpdateQueryGenerator(String tableName) {
        this.tableName = tableName;
    }

    public <ID, T> String update(ID id, T updatedObject) {
        StringBuilder query = new StringBuilder();

        query.append(generateUpdateCondition())
                .append(generateSetCondition(updatedObject))
                .append(generateWhereCondition(id));

        return query.toString();
    }

    private String generateUpdateCondition() {
        return "UPDATE " + tableName;
    }



    private <ID> String generateWhereCondition(ID id) {
        String query =" WHERE id = "; //todo: make id name template
        if(Number.class.isAssignableFrom(id.getClass())) {
            query += id;
        } else {
            query += "'" + id + "'";
        }

        return query;
    }

    private <T> String generateSetCondition(T updatedObject) {
        StringBuilder query = new StringBuilder(" SET ");
        var fields = updatedObject.getClass().getDeclaredFields();
        boolean isFirstCondition = true;

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String rowName = ParsingUtils.reconcatenateCamelCase(fieldName, "_");
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object result;

            try {
                Method method = updatedObject.getClass().getMethod(methodName);
                result = method.invoke(updatedObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(result == null) {
                continue;
            }

            if(!isFirstCondition)
                query.append(", ");
            query.append(rowName); //todo: need to set null if map.getValue() = null
            query.append(" = ");

            if(Number.class.isAssignableFrom(fields[i].getType())) {
                query.append(result);

            } else if (Map.class.isAssignableFrom(fields[i].getType())) {
                Map<String, ? super Object> json = (Map<String, ? super Object>) result;
                boolean isFirstEntry = true;
                query.append("jsonb_build_object(");

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
                query.append(")");
            } else {
                query.append("'"+ result + "'");
            }

            isFirstCondition = false;
        }

        return query.toString();
    }
}
