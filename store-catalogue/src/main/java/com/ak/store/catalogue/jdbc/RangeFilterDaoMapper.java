package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.model.entity.RangeFilter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RangeFilterDaoMapper implements RowMapper<RangeFilter> {

    @Override
    public RangeFilter mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RangeFilter(
                rs.getLong("id"),
                rs.getLong("category_id"),
                rs.getInt("from"),
                rs.getInt("to"));
    }
}