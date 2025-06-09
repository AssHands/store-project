package com.ak.store.synchronization.facade;

import com.ak.store.common.snapshot.catalogue.ProductRatingUpdatedSnapshot;
import com.ak.store.common.snapshot.catalogue.ProductSnapshotPayload;
import com.ak.store.synchronization.service.ProductElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductElasticService productElasticService;

    public void createOne(ProductSnapshotPayload request) {
        productElasticService.createOne(request);
    }

    public void updateOne(ProductSnapshotPayload request) {
        productElasticService.updateOne(request);
    }

    public void updateOneRating(ProductRatingUpdatedSnapshot request) {
        productElasticService.updateOneRating(request);
    }

    public void deleteOne(Long id) {
        productElasticService.deleteOne(id);
    }
}