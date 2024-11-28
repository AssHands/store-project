package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.Product;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDaoMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getFloat("price"),
                rs.getLong("category_id"),
                rs.getInt("amount_reviews"),
                rs.getFloat("grade"));
    }
}