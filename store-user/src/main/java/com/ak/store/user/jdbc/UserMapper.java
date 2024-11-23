//package com.ak.store.user.jdbc;
//
//import com.ak.store.common.entity.user.User;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserMapper implements RowMapper<User> {
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        return new User(rs.getLong("id"),
//                rs.getString("name"),
//                rs.getInt("age"),
//                rs.getDate("created_at"),
//                rs.getDate("updated_at"));
//    }
//}
