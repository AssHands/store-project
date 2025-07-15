package com.ak.store.order.repository;

import com.ak.store.order.model.view.feign.ProductView;

import java.util.List;

public interface ProductRepo {
    List<ProductView> findAllByIds(List<Long> ids);

    boolean isAvailableAllProduct(List<Long> ids);
}