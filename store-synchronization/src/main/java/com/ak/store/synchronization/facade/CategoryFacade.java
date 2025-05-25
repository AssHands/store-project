package com.ak.store.synchronization.facade;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.synchronization.service.CategoryRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryRedisService categoryRedisService;

    public void createOne(CategorySnapshotPayload request) {
        categoryRedisService.createOne(request);
    }

    public void updateOne(CategorySnapshotPayload request) {
        categoryRedisService.updateOne(request);
    }

    public void deleteOne(Long id) {
        categoryRedisService.deleteOne(id);
    }
}