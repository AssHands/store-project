package com.ak.store.synchronization.service;

import com.ak.store.common.snapshot.catalogue.ProductRatingUpdatedSnapshot;
import com.ak.store.common.snapshot.catalogue.ProductSnapshotPayload;
import com.ak.store.synchronization.mapper.ProductMapper;
import com.ak.store.synchronization.repo.elastic.ProductElasticRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductElasticService {
    private final ProductElasticRepo productRepo;
    private final ProductMapper productMapper;

    public void createOne(ProductSnapshotPayload request) {
        productRepo.saveOne(productMapper.toProductDocument(request));
    }

    public void updateOne(ProductSnapshotPayload request) {
        productRepo.updateOne(productMapper.toProductDocument(request));
    }

    public void deleteOne(Long id) {
        productRepo.deleteOne(id);
    }

    public void updateOneRating(ProductRatingUpdatedSnapshot request) {
        productRepo.updateOne(productMapper.toProductDocument(request));
    }
}