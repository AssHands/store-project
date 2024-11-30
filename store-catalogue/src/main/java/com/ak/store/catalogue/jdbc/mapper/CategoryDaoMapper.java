package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDaoMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long parentId = null;
        if(rs.getString("parent_id") != null) {
            parentId = rs.getLong("parent_id");
        }

        return new Category(
                rs.getLong("id"),
                parentId,
                rs.getString("name"));
    }
}
