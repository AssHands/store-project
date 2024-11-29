package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.model.entity.FilterByCharacteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.CharacteristicFilter;
import com.ak.store.common.dto.search.nested.Sort;

import java.util.List;

public interface ProductDao {
    Product findOneById(Long id);

//    Product updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    List<Product> findAllByIds(List<Long> ids, Sort sort);

    List<FilterByCharacteristic> findAllCharacteristicFilters(Long categoryId);
}
