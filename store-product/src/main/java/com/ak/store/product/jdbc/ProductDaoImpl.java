package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.dto.ProductFullDTO;
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
    public List<ProductDTO> findAll(String sort, int offset, int limit,
                                    Map<String, String> filters, Class<?> clazz) {
        System.out.println(queryGenerator.generateQuery(sort, offset, limit, filters, clazz));

        return jdbcTemplate.query(queryGenerator.generateQuery(sort, offset, limit, filters, clazz),
                new ProductMapper(clazz));
    }

    @Override
    public List<ProductDTO> findAll(String sort, int offset, int limit,
                                               Class<?> clazz) {
        System.out.println(queryGenerator.generateQuery(sort, offset, limit, clazz));

        return jdbcTemplate.query(queryGenerator.generateQuery(sort, offset, limit, clazz),
                new ProductMapper(clazz));
    }

    @Override
    public ProductDTO findOneById(Long id, Class<?> clazz) {
        String query = queryGenerator.generateQuery(clazz) + " WHERE id=?";
        System.out.println(query);

        return jdbcTemplate.queryForObject(query, new ProductMapper(clazz), id);
    }
}