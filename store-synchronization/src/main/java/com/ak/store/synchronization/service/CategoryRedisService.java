package com.ak.store.synchronization.service;

import com.ak.store.synchronization.model.document.CategoryDocument;
import com.ak.store.synchronization.repo.redis.CategoryRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    private final CategoryRedisRepo categoryRedisRepo;

    public CategoryDocument createOne(CategoryDocument category) {
        return categoryRedisRepo.save(category);
    }

    public List<CategoryDocument> createAll(List<CategoryDocument> categories) {
        return (List<CategoryDocument>) categoryRedisRepo.saveAll(categories);
    }

    public CategoryDocument updateOne(CategoryDocument category) {
        return categoryRedisRepo.save(category);
    }

    public List<CategoryDocument> updateAll(List<CategoryDocument> categories) {
        return (List<CategoryDocument>) categoryRedisRepo.saveAll(categories);
    }

    public void deleteOne(Long id) {
        categoryRedisRepo.deleteById(id);
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisRepo.deleteAllById(ids);
    }
}
