package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.synchronization.service.ProductElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;

    public void createAll(List<ProductSnapshotPayload> request) {
        productElasticService.createAll(request);
    }

    public void updateAll(List<ProductSnapshotPayload> request) {
        productElasticService.updateAll(request);
    }

    public void deleteAll(List<Long> ids) {
        productElasticService.deleteAll(ids);
    }
}