package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.synchronization.service.ProductElasticService;
import com.ak.store.synchronization.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;
    //private final ProductRedisRepo productRedisRepo;
    private final ProductMapper productMapper;

    public void createOne(ProductDTO product) {
        productElasticService.createOne(productMapper.toProductDocument(product));
    }

    public void createAll(List<ProductDTO> products) {
        productElasticService.createAll(productMapper.toProductDocument(products));
        //productRedisRepo.saveAll(productMapper.toProductDocument(products));
    }

    public void updateOne(ProductDTO product) {
        productElasticService.updateOne(productMapper.toProductDocument(product));
    }

    public void updateAll(List<ProductDTO> products) {
        productElasticService.updateAll(productMapper.toProductDocument(products));
    }

    public void deleteOne(Long id) {
        productElasticService.deleteOne(id);
    }

    public void deleteAll(List<Long> ids) {
        productElasticService.deleteAll(ids);
    }
}
