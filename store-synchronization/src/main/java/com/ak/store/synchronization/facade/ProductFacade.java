package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.synchronization.elastic.ElasticService;
import com.ak.store.synchronization.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ElasticService elasticService;
    private final ProductMapper productMapper;

    @Transactional
    public void createOneProduct(ProductDTO product) {
        elasticService.createOneProduct(productMapper.toProductDocument(product));
    }

    @Transactional
    public void updateOneProduct(ProductDTO product) {
        elasticService.updateOneProduct(productMapper.toProductDocument(product));
    }

    @Transactional
    public void deleteOneProduct(Long id) {
        elasticService.deleteOneProduct(id);
    }
}
