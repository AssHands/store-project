package com.ak.store.catalogue.jdbc;

import com.ak.store.common.dto.product.ProductDTO;

import java.util.List;

public interface ProductDao {
    ProductDTO findOneById(Long id);

//    ProductDTO updateOneById(Long id, ProductPayload updatedProduct);
//
//    boolean deleteOneById(Long id);

    List<ProductDTO> findAllByIds(List<Long> ids);
}
