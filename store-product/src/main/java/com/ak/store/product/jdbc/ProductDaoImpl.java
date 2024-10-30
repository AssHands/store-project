package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDto;
import com.ak.store.queryGenerator.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
 public class ProductDaoImpl implements ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final QueryGenerator queryGenerator;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate, QueryGenerator queryGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryGenerator = queryGenerator;
    }

    @Override
    public List<ProductDto> findAll(String sort, int offset, int limit,
                                    Map<String, String> filters) {
        System.out.println(queryGenerator.generateQuery(sort, offset, limit, filters));

        return jdbcTemplate.query(queryGenerator.generateQuery(sort, offset, limit, filters),
                new ProductMapper());
    }

    @Override
    public List<ProductDto> findAll(String sort, int offset, int limit) {
        System.out.println(queryGenerator.generateQuery(sort, offset, limit));

        return jdbcTemplate.query(queryGenerator.generateQuery(sort, offset, limit),
                new ProductMapper());
    }
}