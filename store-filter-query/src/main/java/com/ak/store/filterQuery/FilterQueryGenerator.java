package com.ak.store.filterQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class FilterQueryGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateQuery(String jsonString) {

        try {
            // Парсинг JSON в объект JsonNode
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // Создание WHERE-условия для SQL-запроса
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
                sql.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
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
            if (!firstCondition) {
                sql.append(" AND ");
            }
            sql.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            firstCondition = false;
        }
        return sql.toString();
    }
}