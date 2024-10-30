package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductPreviewDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductPreviewMapper implements RowMapper<ProductPreviewDTO> {

    @Override
    public ProductPreviewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductPreviewDTO(rs.getLong("id"),
                rs.getString("title"),
                rs.getFloat("price"),
                rs.getInt("category_id"));
    }
}
