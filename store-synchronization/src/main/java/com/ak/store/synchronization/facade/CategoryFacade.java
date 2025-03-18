package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.synchronization.redis.CategoryRedisRepo;
import com.ak.store.synchronization.util.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryMapper categoryMapper;
    private final CategoryRedisRepo categoryRedisRepo;

    public void createOne(CategoryDTO category) {
        categoryRedisRepo.save(
                categoryMapper.toCategoryDocument(category));
    }

    public void createAll(List<CategoryDTO> categories) {
        categoryRedisRepo.saveAll(
                categoryMapper.toCategoryDocument(categories));
    }

    public void updateOne(CategoryDTO category) {
        categoryRedisRepo.save(
                categoryMapper.toCategoryDocument(category));
    }

    public void updateAll(List<CategoryDTO> categories) {
        categoryRedisRepo.saveAll(
                categoryMapper.toCategoryDocument(categories));
    }

    public void deleteOne(Long id) {
        categoryRedisRepo.deleteById(id);
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisRepo.deleteAllById(ids);
    }
}