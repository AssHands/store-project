package com.ak.store.synchronization.service;

import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;
import com.ak.store.synchronization.mapper.CategoryMapper;
import com.ak.store.synchronization.repository.postgres.CategoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Transactional
    public void createOne(CategorySnapshotPayload request) {
        var category = categoryMapper.toCategory(request);
        categoryRepo.save(category);
    }

    @Transactional
    public void updateOne(CategorySnapshotPayload request) {
        var category = categoryMapper.toCategory(request);
        categoryRepo.save(category);
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryRepo.deleteById(id);
    }
}