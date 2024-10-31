package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<ProductDTO> {

    private final Class<?> clazz;

    public ProductMapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        if(clazz.equals(ProductPreviewDTO.class)) {
            return new ProductPreviewDTO(rs.getLong("id"),
                    rs.getString("title"),
                    rs.getFloat("price"),
                    rs.getInt("category_id"));
            
        } else if(clazz.equals(ProductFullDTO.class)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return new ProductFullDTO(rs.getLong("id"), 
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getFloat("price"),
                        rs.getInt("category_id"),
                        mapper.readValue(rs.getString("properties"), new TypeReference<>() {}));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } 
        }
        
        throw new RuntimeException("Unknown class in ProductMapper");
    }
}