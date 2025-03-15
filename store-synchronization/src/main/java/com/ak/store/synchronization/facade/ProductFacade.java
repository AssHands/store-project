package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.synchronization.elastic.ProductElasticService;
import com.ak.store.synchronization.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;
    private final ProductMapper productMapper;

    public void createOneProduct(ProductDTO product) {
        productElasticService.createOneProduct(productMapper.toProductDocument(product));
    }

    public void updateOneProduct(ProductDTO product) {
        productElasticService.updateOneProduct(productMapper.toProductDocument(product));
    }

    public void deleteOneProduct(Long id) {
        productElasticService.deleteOneProduct(id);
    }
}
