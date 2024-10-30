package com.ak.store.product.service;


import com.ak.store.common.ResponseObject.ProductPageResponse;
import com.ak.store.common.entity.product.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductPageResponse> findAll(String sort, int offset, int limit,
                                      Map<String, String> filters);


}
