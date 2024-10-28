package com.ak.store.product.jdbc;

import com.ak.store.common.ResponseObject.ProductResponse;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductResponse> a(Map<String, String> a);
}
