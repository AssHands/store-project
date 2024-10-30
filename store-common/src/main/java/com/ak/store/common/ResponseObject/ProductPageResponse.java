package com.ak.store.common.ResponseObject;

import com.ak.store.common.entity.product.Product;

import java.util.List;

public class ProductPageResponse {
    private List<ProductResponse> content;

    private boolean last;
    private int totalPages;
    private Long totalElements;
}
