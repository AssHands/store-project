package com.ak.store.synchronization.facade;

import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;
import com.ak.store.synchronization.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryService categoryService;

    @Transactional
    public void createOne(CategorySnapshotPayload request) {
        categoryService.createOne(request);
    }

    @Transactional
    public void updateOne(CategorySnapshotPayload request) {
        categoryService.updateOne(request);
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryService.deleteOne(id);
    }
}