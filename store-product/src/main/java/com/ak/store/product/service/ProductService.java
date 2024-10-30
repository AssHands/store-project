package com.ak.store.product.service;


import com.ak.store.common.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDto> findAll(String sort, int offset, int limit,
                             Map<String, String> filters);
}
