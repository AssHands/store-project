package com.ak.store.filterQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterQueryGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateQuery(String jsonString) {

        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);


            Map<String, String> whereConditions = new HashMap<>();
            rootNode.fieldNames().forEachRemaining(fieldName -> {
                JsonNode fieldValue = rootNode.path(fieldName);
                whereConditions.put(fieldName, fieldValue.asText());
            });

            // Формирование SQL-запроса
            StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE ");
            boolean firstCondition = true;
            for (Map.Entry<String, String> entry : whereConditions.entrySet()) {
                if (!firstCondition) {
                    sql.append(" AND ");
                }
                sql.append("(").append(entry.getKey()).append(" = ").append(entry.getValue()).append(")");
                firstCondition = false;
            }
            return sql.toString();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при преобразовании JSON в SQL: " + e.getMessage());
        }

    }

    public static String generateQueryMap(HashMap<String, String> filters) {
        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE ");
        boolean firstCondition = true;
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (!firstCondition) sql.append(" AND ");


            sql.append(entry.getKey());
            sql.append(" = ");

            List<String> a = getValidFilters(entry.getValue());
            for(int i = 0; i < a.size(); i++) {
                if(i != a.size() - 1) {
                    sql.append(a.get(i)).append();

                }

            }

            firstCondition = false;

        }
        return sql.toString();
    }

    public static List<String> getValidFilters(String string) {
        return Arrays.stream(string.split(":"))
                .filter(flt -> flt.matches("^[0-9]+$"))
                .distinct()
                .toList();
    }
}