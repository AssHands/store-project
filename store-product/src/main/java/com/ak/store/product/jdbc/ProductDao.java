package com.ak.store.product.jdbc;

import com.ak.store.common.ResponseObject.ProductResponse;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductResponse> findAll(String sort, int limit, int offset,
                                  Map<String, String> filters);
}
