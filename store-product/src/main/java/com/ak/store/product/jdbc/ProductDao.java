package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.payload.ProductPayload;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductDTO> findAll(String sort, int offset, int limit,
                             Map<String, String> filters, Class<?> clazz);

    List<ProductDTO> findAll(String sort, int offset, int limit, Class<?> clazz);

    ProductDTO findOneById(Long id, Class<?> clazz);

    ProductDTO updateOneById(Long id, Map<String, ? super Object> updatedFields);
    ProductDTO updateOneById(Long id, ProductPayload productPayload);
}
