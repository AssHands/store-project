package com.ak.store.catalogue.jdbc;

import com.ak.store.common.dto.product.ProductDTO;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<ProductDTO> {

    @Override
    public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductDTO(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getFloat("price"),
                rs.getLong("category_id"),
                rs.getInt("amount_reviews"),
                rs.getFloat("grade"));
    }
}