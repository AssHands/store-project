package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.CharacteristicFilter;
import com.ak.store.catalogue.model.entity.FilterByCharacteristic;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterByCharacteristicDaoMapper implements RowMapper<FilterByCharacteristic> {

    @Override
    public FilterByCharacteristic mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilterByCharacteristic(
                rs.getLong("id"),
                rs.getInt("from_value"),
                rs.getInt("to_value"),
                rs.getString("text_value"),
                rs.getLong("characteristic_id"),
                rs.getString("name"));
    }
}