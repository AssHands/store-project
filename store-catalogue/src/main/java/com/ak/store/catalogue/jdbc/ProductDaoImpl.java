package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.jdbc.mapper.CategoryDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.CharacteristicByCategoryDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.FilterByCharacteristicDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.ProductDaoMapper;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CharacteristicByCategory;
import com.ak.store.catalogue.model.entity.FilterByCharacteristic;
import com.ak.store.catalogue.model.entity.Product;
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
            //default -> query += "ORDER BY amount_sales DESC"; //POPULAR todo
        }

        System.out.println(query);

        return namedJdbcTemplate.query(query, Map.of("ids", ids),
                new ProductDaoMapper());
    }

    //todo: should i create new dao or make all in one?
    @Override
    public List<FilterByCharacteristic> findAllCharacteristicFilters(Long categoryId) {
        String query = """
                       SELECT f.id, f.from_value, f.to_value, f.text_value, cf.characteristic_id, c.name
                       FROM filter f
                       JOIN characteristic_filter cf
                       ON f.id = cf.filter_id
                       JOIN category_characteristic cc
                       ON cc.characteristic_id = cf.characteristic_id
                       JOIN characteristic c
                       ON c.id = cc.characteristic_id
                       WHERE cc.category_id=:categoryId
                       ORDER BY f.from_value
                       """;

        System.out.println(query);

        return namedJdbcTemplate.query(query, Map.of("categoryId", categoryId),
                new FilterByCharacteristicDaoMapper());
    }

    @Override
    public List<Category> findAllCategory() {
        String query = "SELECT * FROM category";
        System.out.println(query);

        return namedJdbcTemplate.query(query, new CategoryDaoMapper());
    }

    @Override
    public List<Category> findAllCategoryByName(String name) {
        String query = "SELECT * FROM category WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))";
        System.out.println(query);

        return namedJdbcTemplate.query(query, Map.of("name", name), new CategoryDaoMapper());
    }

    @Override
    public List<CharacteristicByCategory> findAllAvailableCharacteristic(Long categoryId) {
        String query = """
                       SELECT f.id, f.text_value, cf.characteristic_id, c.name
                       FROM filter f
                       JOIN characteristic_filter cf
                       ON f.id = cf.filter_id
                       JOIN category_characteristic cc
                       ON cc.characteristic_id = cf.characteristic_id
                       JOIN characteristic c
                       ON c.id = cc.characteristic_id
                       WHERE cc.category_id=:categoryId
                       """;

        System.out.println(query);

        return namedJdbcTemplate.query(query, Map.of("categoryId", categoryId),
                new CharacteristicByCategoryDaoMapper());
    }
}