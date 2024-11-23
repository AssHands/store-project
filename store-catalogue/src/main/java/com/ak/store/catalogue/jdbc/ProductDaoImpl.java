package com.ak.store.catalogue.jdbc;

import com.ak.store.common.dto.product.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
 public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    public ProductDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public ProductDTO findOneById(Long id) {
        String query = "SELECT * FROM product WHERE id=:id";
        System.out.println(query);

        return namedParameterJdbcTemplate.queryForObject(query, Map.of("id", id), new ProductMapper());
    }

//    @Override
//    public ProductDTO updateOneById(Long id, ProductPayload updatedProduct) {
//        System.out.println(updateQueryGenerator.update(id, updatedProduct));
//        jdbcTemplate.update(updateQueryGenerator.update(id, updatedProduct));
//        return findOneById(id, ProductDTO.class);
//    }

//    @Override
//    public boolean deleteOneById(Long id) {
//        String query = "DELETE FROM product_new WHERE id=?";
//        return jdbcTemplate.update(query, id) == 1;
//    }

    @Override
    public List<ProductDTO> findAllByIds(List<Long> ids) {
        String query = "SELECT * FROM product WHERE id IN (:ids)";
        System.out.println(query);

        return namedParameterJdbcTemplate.query(query, Map.of("ids", ids), new ProductMapper());
    }
}