package com.ak.store.product.service;

import com.ak.store.common.ResponseObject.ProductPageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<ProductPageResponse> findAll(String sort, int offset, int limit,
                                             Map<String, String> filters) {

        return null;
    }
}