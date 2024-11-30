package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.CharacteristicByCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacteristicByCategoryDaoMapper implements RowMapper<CharacteristicByCategory> {

    @Override
    public CharacteristicByCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        boolean isTextCharacteristic = false;
        if(rs.getString("text_value") != null) {
            isTextCharacteristic = true;
        }

        return new CharacteristicByCategory(
                rs.getLong("id"),
                rs.getLong("characteristic_id"),
                rs.getString("name"),
                rs.getString("text_value"));
    }
}
