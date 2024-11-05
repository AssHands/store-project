package com.ak.store.product.service;


import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.payload.ProductPayload;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDTO> findAll(String sort, int offset, int limit,
                             Map<String, String> filters, Class<?> clazz);

    ProductDTO findOneById(Long id, Class<?> clazz);

    ProductDTO updateOneById(Long id, ProductPayload updatedProduct);
}
