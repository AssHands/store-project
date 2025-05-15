package com.ak.store.synchronization.facade;

import com.ak.store.synchronization.service.ProductElasticService;
import com.ak.store.synchronization.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;
    private final ProductMapper productMapper;

    public void createAll(List<ProductDTO> products) {
        productElasticService.createAll(productMapper.toProductDocument(products));
    }

    public void updateAll(List<ProductDTO> products) {
        productElasticService.updateAll(productMapper.toProductDocument(products));
    }

    public void deleteAll(List<Long> ids) {
        productElasticService.deleteAll(ids);
    }
}