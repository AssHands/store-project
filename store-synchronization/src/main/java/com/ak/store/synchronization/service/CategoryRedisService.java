package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.document.CategoryDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryRedisService {
    //private final CategoryRedisRepo categoryRedisRepo;

    public CategoryDocument createOne(CategoryDocument category) {
        return null; //categoryRedisRepo.save(category);
    }

    public List<CategoryDocument> createAll(List<CategoryDocument> categories) {
        return null; //(List<CategoryDocument>) categoryRedisRepo.saveAll(categories);
    }

    public CategoryDocument updateOne(CategoryDocument category) {
        return null; //categoryRedisRepo.save(category);
    }

    public List<CategoryDocument> updateAll(List<CategoryDocument> categories) {
        return null; //(List<CategoryDocument>) categoryRedisRepo.saveAll(categories);
    }

    public void deleteOne(Long id) {
        //categoryRedisRepo.deleteById(id);
    }

    public void deleteAll(List<Long> ids) {
        //categoryRedisRepo.deleteAllById(ids);
    }
}
