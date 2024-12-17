package com.ak.store.catalogue.jdbc;

;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component //todo: difference between component and repository annotations
 public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    public ProductDaoImpl(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

}