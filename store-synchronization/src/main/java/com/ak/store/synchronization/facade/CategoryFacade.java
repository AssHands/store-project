package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.service.CategoryRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryRedisService categoryRedisService;

    public void createAll(List<CategorySnapshotPayload> request) {
        categoryRedisService.createAll(request);
    }

    public void updateAll(List<CategorySnapshotPayload> request) {
        categoryRedisService.updateAll(request);
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisService.deleteAll(ids);
    }
}