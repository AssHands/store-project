package com.ak.store.catalogue.jdbc;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.common.payload.search.nested.Sort;

import java.util.List;

public interface ProductDao {
    Product findOneById(Long id);

//    Product updateOneById(Long id, ProductPayload updatedProduct);

    boolean deleteOneById(Long id);

    List<Product> findAllByIds(List<Long> ids, Sort sort);
}
