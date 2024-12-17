package com.ak.store.catalogue.jdbc.mapper;

import com.ak.store.catalogue.model.entity.CharacteristicByCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacteristicByCategoryDaoMapper implements RowMapper<CharacteristicByCategory> {

    @Override
    public CharacteristicByCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
