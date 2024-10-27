package com.ak.store.user.jdbc;

import com.ak.store.common.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.util.ParsingUtils;

import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    private String getValidatedField(String field) {
//        List<String> allowList = new ArrayList<>(5);
//        allowList.addAll(List.of("id", "name", "age", "createdAt", "updatedAt"));
//
//        if(!allowList.contains(field)) {
//            throw new RuntimeException("Unknown field");
//        }
//
//        return ParsingUtils.reconcatenateCamelCase(field, "_");
//    }
//
//    private int defineType(String value) {
//        if(value.matches("^[0-9]+$")) {
//            return Types.INTEGER;
//        } else if (value.matches("^[a-zA-Zа-яА-Я\\s.]+$")) {
//            return Types.VARCHAR;
//        } else if(value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
//            return Types.DATE;
//        }
//
//        throw new RuntimeException("Incorrect value");
//    }
}