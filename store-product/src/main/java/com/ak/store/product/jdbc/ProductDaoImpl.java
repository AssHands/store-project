package com.ak.store.product.jdbc;

import com.ak.store.common.ResponseObject.ProductResponse;
import com.ak.store.filterQuery.FilterQueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
 public class ProductDaoImpl implements ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilterQueryGenerator filterQueryGenerator;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate, FilterQueryGenerator filterQueryGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.filterQueryGenerator = filterQueryGenerator;
    }

    @Override
    public List<ProductResponse> a(Map<String, String> a) {
        return jdbcTemplate.query(filterQueryGenerator.generateQueryWithFilters(a), new ProductMapper());
    }

//    private String getValidatedField(String field) {
//        List<String> allowList = new ArrayList<>(5);
//        allowList.addAll(List.of("id", "name", "age", "createdAt", "updatedAt"));
//
//        if(!allowList.contains(field)) {
//            throw new RuntimeException("Unknown field");
//        }
//
//        return ParsingUtils.reconcatenateCamelCase(field, "_");
//    }
//
//    private int defineType(String value) {
//        if(value.matches("^[0-9]+$")) {
//            return Types.INTEGER;
//        } else if (value.matches("^[a-zA-Zа-яА-Я\\s.]+$")) {
//            return Types.VARCHAR;
//        } else if(value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
//            return Types.DATE;
//        }
//
//        throw new RuntimeException("Incorrect value");
//    }
}