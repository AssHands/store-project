package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<ProductDto> {

    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ProductDto(rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getFloat("price"),
                    rs.getInt("category_id"),
                    mapper.readValue(rs.getString("properties"), new TypeReference<>() {}));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}