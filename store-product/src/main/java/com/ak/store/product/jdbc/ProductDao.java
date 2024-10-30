package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductDto> findAll(String sort, int offset, int limit,
                             Map<String, String> filters);
    List<ProductDto> findAll(String sort, int offset, int limit);
}
