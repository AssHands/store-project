package com.ak.store.order.repository;

import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.model.feign.ProductView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RestProductRepo implements ProductRepo {
    private final CatalogueFeign catalogueFeign;

    @Override
    public List<ProductView> findAllByIds(List<Long> ids) {
        return catalogueFeign.findAllProductByIds(ids);
    }

    @Override
    public boolean isAvailableAllProduct(List<Long> ids) {
        return catalogueFeign.isAvailableAllProduct(ids);
    }
}