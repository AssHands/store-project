package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.CharacteristicFilter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacteristicFilterDaoMapper implements RowMapper<CharacteristicFilter> {

    @Override
    public CharacteristicFilter mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CharacteristicFilter(
                rs.getLong("id"),
                rs.getLong("characteristic_id"),
                rs.getLong("category_id"),
                rs.getInt("from_range"),
                rs.getInt("to_range"),
                rs.getBoolean("is_text_filter"));
    }
}