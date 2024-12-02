package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.dto.search.nested.Sort;
import com.ak.store.common.payload.product.ProductWritePayload;

import java.util.List;

public interface ProductDao {
    Product findOneById(Long id);

//    Product updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    List<Product> findAllByIds(List<Long> ids, Sort sort);

    List<FilterByCharacteristic> findAllCharacteristicFilters(Long categoryId);

    List<Category> findAllCategory();

    List<Category> findAllCategoryByName(String name);

    List<CharacteristicByCategory> findAllAvailableCharacteristic(Long categoryId);

    boolean createOne(ProductWritePayload productPayload);
    boolean updateOne(ProductWritePayload productPayload, Long productId);
}
