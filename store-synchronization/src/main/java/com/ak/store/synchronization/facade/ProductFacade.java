package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.synchronization.mapper.ProductMapper;
import com.ak.store.synchronization.service.ProductElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;
    private final ProductMapper productMapper;

    public void createOne(ProductSnapshotPayload request) {
        productElasticService.createOne(request);
    }

    public void updateOne(ProductSnapshotPayload request) {
        productElasticService.updateOne(request);
    }

    public void deleteOne(Long id) {
        productElasticService.deleteOne(id);
    }
}