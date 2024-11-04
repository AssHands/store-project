package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.payload.ProductPayload;
import com.ak.store.queryGenerator.SelectQueryGenerator;
import com.ak.store.queryGenerator.UpdateQueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
 public class ProductDaoImpl implements ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SelectQueryGenerator selectQueryGenerator;
    private final UpdateQueryGenerator<Long> updateQueryGenerator;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate, SelectQueryGenerator selectQueryGenerator,
                          UpdateQueryGenerator<Long> updateQueryGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.selectQueryGenerator = selectQueryGenerator;
        this.updateQueryGenerator = updateQueryGenerator;
    }

    @Override
    public List<ProductDTO> findAll(String sort, int offset, int limit,
                                    Map<String, String> filters, Class<?> clazz) {
        System.out.println(selectQueryGenerator.select(sort, offset, limit, filters, clazz));

        return jdbcTemplate.query(selectQueryGenerator.select(sort, offset, limit, filters, clazz),
                new ProductMapper(clazz));
    }

    @Override
    public List<ProductDTO> findAll(String sort, int offset, int limit,
                                               Class<?> clazz) {
        System.out.println(selectQueryGenerator.select(sort, offset, limit, clazz));

        return jdbcTemplate.query(selectQueryGenerator.select(sort, offset, limit, clazz),
                new ProductMapper(clazz));
    }

    @Override
    public ProductDTO findOneById(Long id, Class<?> clazz) {
        String query = selectQueryGenerator.select(clazz) + " WHERE id=?";
        System.out.println(query);

        return jdbcTemplate.queryForObject(query, new ProductMapper(clazz), id);
    }

    @Override
    public ProductDTO updateOneById(Long id, Map<String, ? super Object> updatedFields) {
        System.out.println(updateQueryGenerator.update(id, updatedFields));
        jdbcTemplate.update(updateQueryGenerator.update(id, updatedFields));
        return null;
    }

    @Override
    public ProductDTO updateOneById(Long id, ProductPayload productPayload) {
        System.out.println(updateQueryGenerator.update(id, productPayload));
        jdbcTemplate.update(updateQueryGenerator.update(id, productPayload));
    }
}