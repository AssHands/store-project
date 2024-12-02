package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.jdbc.mapper.CategoryDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.CharacteristicByCategoryDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.FilterByCharacteristicDaoMapper;
import com.ak.store.catalogue.jdbc.mapper.ProductDaoMapper;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CharacteristicByCategory;
import com.ak.store.catalogue.model.entity.FilterByCharacteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.dto.catalogue.others.CharacteristicsWriteDTO;
import com.ak.store.common.dto.catalogue.others.nested.NumericCharacteristic;
import com.ak.store.common.dto.catalogue.others.nested.TextCharacteristic;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.dto.search.nested.Sort;
import com.ak.store.common.payload.product.ProductWritePayload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.HashMap;
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

        return amountDeleted != 0;
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

    @Override
    @Transactional //todo: check how it works
    public boolean createOne(ProductWritePayload productPayload) {
        Long id = createProduct(productPayload.getProductWriteDTO());
        createProductCharacteristics(productPayload.getCreateCharacteristics(), id);

        return true;
    }

    @Override
    @Transactional
    public boolean updateOne(ProductWritePayload productPayload, Long productId) {
        updateProduct(productPayload.getProductWriteDTO(), productId);
        updateProductCharacteristics(productPayload.getUpdateCharacteristics(), productId);
        deleteProductCharacteristics(productPayload.getDeleteCharacteristics(), productId);
        createProductCharacteristics(productPayload.getCreateCharacteristics(), productId);
        return true;
    }

    private void updateProduct(ProductWriteDTO productDTO, Long productId) {
        StringBuilder query = new StringBuilder("UPDATE product SET ");
        Map<String, Object> params = new HashMap<>();

        boolean first = true;
        if (productDTO.getTitle() != null) {
            query.append(first ? "" : ", ").append("title = :title");
            params.put("title", productDTO.getTitle());
            first = false;
        }
        if (productDTO.getDescription() != null) {
            query.append(first ? "" : ", ").append("description = :description");
            params.put("description", productDTO.getDescription());
            first = false;
        }
        if (productDTO.getPrice() != null) {
            query.append(first ? "" : ", ").append("price = :price");
            params.put("price", productDTO.getPrice());
            first = false;
        }
        if(productDTO.getCategoryId() != null) {
            query.append(first ? "" : ", ").append("category_id = :categoryId");
            params.put("categoryId", productDTO.getCategoryId());
        }

        if(first)
            return;

        query.append(" WHERE id = :id");
        params.put("id", productId);

        System.out.println(query);
        namedJdbcTemplate.update(query.toString(), params);
    }

    private void deleteProductCharacteristics(CharacteristicsWriteDTO characteristicsDTO, Long productId) {
        String query = """
                DELETE FROM product_characteristic
                WHERE product_id = :productId and characteristic_id IN (:ids)
                """;

        List<Long> ids = new ArrayList<>();

        for (var characteristic : characteristicsDTO.getTextCharacteristics()) {
            ids.add(characteristic.getCharacteristicId());
        }

        for (var characteristic : characteristicsDTO.getNumericCharacteristics()) {
            ids.add(characteristic.getCharacteristicId());
        }

        if(ids.isEmpty())
            return;

        System.out.println(query);
        namedJdbcTemplate.update(query, Map.of("productId", productId, "ids", ids));
    }

    private void updateProductCharacteristics(CharacteristicsWriteDTO characteristicsDTO, Long productId) {
        String query = """
                        UPDATE product_characteristic
                        SET text_value = CASE WHEN :isText THEN :value ELSE NULL END,
                        numeric_value = CASE WHEN NOT isText THEN CAST(:value AS INTEGER) ELSE NULL END
                        WHERE product_id = :productId AND characteristic_id = :characteristicId
                        """;

        List<TextCharacteristic> textCharacteristics = characteristicsDTO.getTextCharacteristics();
        List<NumericCharacteristic> numericCharacteristics = characteristicsDTO.getNumericCharacteristics();
        int batchSize = textCharacteristics.size() + numericCharacteristics.size();

        if(batchSize == 0)
            return;

        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[batchSize];

        int index = 0;

        for (TextCharacteristic characteristic : textCharacteristics) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", productId);
            params.addValue("characteristicId", characteristic.getCharacteristicId());
            params.addValue("isText", true);
            params.addValue("isNumeric", false);
            params.addValue("value", characteristic.getValue());
            batchParams[index++] = params;
        }

        for (NumericCharacteristic characteristic : numericCharacteristics) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", productId);
            params.addValue("characteristicId", characteristic.getCharacteristicId());
            params.addValue("isText", false);
            params.addValue("isNumeric", true);
            params.addValue("value", characteristic.getValue());
            batchParams[index++] = params;
        }

        System.out.println(query);
        namedJdbcTemplate.batchUpdate(query, batchParams);
    }

    private Long createProduct(ProductWriteDTO productDTO) {
        String query = """
                       INSERT INTO product (title, description, price, category_id)
                       VALUES(:title, :description, :price, :categoryId)
                       RETURNING id
                       """;

        System.out.println(query);
        return namedJdbcTemplate.queryForObject(query,
                Map.of("title", productDTO.getTitle(),
                        "description", productDTO.getDescription(),
                        "price", productDTO.getPrice(),
                        "categoryId", productDTO.getCategoryId()),
                Long.class);
    }

    private void createProductCharacteristics(CharacteristicsWriteDTO characteristicsDTO, Long productId) {
        String query = """
                        INSERT INTO product_characteristic (product_id, characteristic_id, text_value, numeric_value)
                        VALUES (:productId, :characteristicId,
                                CASE WHEN :isText THEN :value ELSE NULL END,
                                CASE WHEN :isNumeric THEN CAST(:value AS INTEGER) ELSE NULL END)
                        """;

        List<TextCharacteristic> textCharacteristics = characteristicsDTO.getTextCharacteristics();
        List<NumericCharacteristic> numericCharacteristics = characteristicsDTO.getNumericCharacteristics();
        int batchSize = textCharacteristics.size() + numericCharacteristics.size();

        if(batchSize == 0)
            return;

        MapSqlParameterSource[] batchParams = new MapSqlParameterSource[batchSize];

        int index = 0;

        for (TextCharacteristic characteristic : textCharacteristics) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", productId);
            params.addValue("characteristicId", characteristic.getCharacteristicId());
            params.addValue("isText", true);
            params.addValue("isNumeric", false);
            params.addValue("value", characteristic.getValue());
            batchParams[index++] = params;
        }

        for (NumericCharacteristic characteristic : numericCharacteristics) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", productId);
            params.addValue("characteristicId", characteristic.getCharacteristicId());
            params.addValue("isText", false);
            params.addValue("isNumeric", true);
            params.addValue("value", characteristic.getValue());
            batchParams[index++] = params;
        }

        System.out.println(query);
        namedJdbcTemplate.batchUpdate(query, batchParams);
    }
}