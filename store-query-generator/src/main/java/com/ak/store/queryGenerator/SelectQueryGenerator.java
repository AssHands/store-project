package com.ak.store.queryGenerator;

import org.springframework.data.util.ParsingUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectQueryGenerator {
    private final String tableName;
    private final String separatorSymbol;
    private final Class<?> type;
    private final String mapFieldName;

    public SelectQueryGenerator(String tableName, String separatorSymbol, Class<?> type) {
        this.tableName = tableName;
        this.separatorSymbol = separatorSymbol;
        this.type = type;
        this.mapFieldName = getNameMapField();
    }

    public SelectQueryGenerator(String separatorSymbol, Class<?> type) {
        this.separatorSymbol = separatorSymbol;
        this.type = type;
        this.tableName = getNameClass();
        this.mapFieldName = getNameMapField();
    }

    public String select(Class<?> clazz) {
        return generateSelectCondition(clazz);
    }

    public String select(String sort, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateSortCondition(sort);
    } 

    public String select(String sort, int offset, int limit, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateSortCondition(sort) +
                generatePaginationCondition(offset, limit);
    }

    public String select(String sort, int offset, int limit,
                         Map<String, String> filters, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateFilterCondition(filters, clazz) +
                generateSortCondition(sort) +
                generatePaginationCondition(offset, limit);
    }

    public String select(Map<String, String> filters, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generateFilterCondition(filters, clazz);
    }

    public String select(int offset, int limit, Class<?> clazz) {

        return generateSelectCondition(clazz) +
                generatePaginationCondition(offset, limit);
    }

    private String generateSortCondition(String sort) {
        return " ORDER BY " + sort;
    }

    private String generatePaginationCondition(int offset, int limit) {
        return " LIMIT " + limit + " OFFSET " + offset;
    }

    private String generateSelectCondition(Class<?> clazz) {
        return "SELECT " + Arrays.stream(clazz.getDeclaredFields())
                .map(field -> ParsingUtils.reconcatenateCamelCase(field.getName(), "_"))
                .collect(Collectors.joining(", ")) + " FROM " + tableName;
    }

    private String generateFilterCondition(Map<String, String> filters, Class<?> clazz) {
        StringBuilder query = new StringBuilder(" WHERE ");
        boolean firstCondition = true;

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (!firstCondition)
                query.append(" AND ");

            query.append("jsonb_extract_path_text(")
                    .append(mapFieldName)
                    .append(", '")
                    .append(entry.getKey())
                    .append("')::integer IN (")
                    .append(getSeparatedValue(entry.getValue()))
                    .append(")");

            firstCondition = false;
        }
        return query.toString();
    }

    private String getSeparatedValue(String value) {
        return Arrays.stream(value.split(separatorSymbol))
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String getNameMapField() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> Map.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .findFirst().orElseThrow(() -> new RuntimeException("Did not find any fields with type Map"));
    }

    private String getNameClass() {
        return type.getSimpleName().toLowerCase();
    }
}