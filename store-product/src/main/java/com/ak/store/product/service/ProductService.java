package com.ak.store.product.service;


import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductFullDTO> findAll(String sort, int offset, int limit,
                                     Map<String, String> filters);

    ProductFullDTO findById(Long id);
}
