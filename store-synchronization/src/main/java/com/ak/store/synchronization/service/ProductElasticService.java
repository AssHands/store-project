package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.synchronization.repo.elastic.ProductElasticRepo;
import com.ak.store.synchronization.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductElasticService {
    private final ProductElasticRepo productRepo;
    private final ProductMapper productMapper;

    public void createAll(List<ProductSnapshotPayload> request) {
        productRepo.saveAll(productMapper.toProductDocument(request));
    }

    public void updateAll(List<ProductSnapshotPayload> request) {
        productRepo.updateAll(productMapper.toProductDocument(request));
    }

    public void deleteAll(List<Long> ids) {
        productRepo.deleteAll(ids);
    }
}