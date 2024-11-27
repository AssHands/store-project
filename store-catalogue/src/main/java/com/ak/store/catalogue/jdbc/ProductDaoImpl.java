package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.RangeFilter;
import com.ak.store.common.dto.search.nested.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
 public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    public ProductDaoImpl(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Product findOneById(Long id) {
        String query = "SELECT * FROM product WHERE id=:id";
        System.out.println(query);

        return namedJdbcTemplate.queryForObject(query, Map.of("id", id),
                new ProductDaoMapper());
    }

//    @Override
//    public ProductDTO updateOneById(Long id, ProductPayload updatedProduct) {
//        System.out.println(updateQueryGenerator.update(id, updatedProduct));
//        jdbcTemplate.update(updateQueryGenerator.update(id, updatedProduct));
//        return findOneById(id, ProductDTO.class);
//    }

    @Override
    public boolean deleteOneById(Long id) {
        String query = "DELETE FROM product WHERE id=?";
        int amountDeleted = namedJdbcTemplate.update(query, Map.of("id", id));

        if(amountDeleted != 0)
            return true;

        return false;
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids, Sort sort) {
        String query = "SELECT * FROM product WHERE id IN (:ids) ";

        switch (sort) {
            case PRICE_UP -> query += "ORDER BY price";
            case PRICE_DOWN -> query += "ORDER BY price DESC";
            case RATING -> query += "ORDER BY grade DESC, amount_reviews DESC";
            default -> query += "ORDER BY price";
            //default -> query += "ORDER BY amount_sales DESC"; //POPULAR
        }

        System.out.println(query);

        return namedJdbcTemplate.query(query, Map.of("ids", ids),
                new ProductDaoMapper());
    }

    //todo: should i create new dto or make all in one?
    @Override
    public List<RangeFilter> findAllRangeFilters(Long categoryId) {
        String query = "SELECT * FROM range_filter WHERE category_id=:categoryId";
        return namedJdbcTemplate.query(query, Map.of("categoryId", categoryId),
                new RangeFilterDaoMapper());
    }
}