package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.synchronization.service.CategoryRedisService;
import com.ak.store.synchronization.util.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryMapper categoryMapper;
    private final CategoryRedisService categoryRedisService;

    public void createOne(CategoryDTO category) {
        categoryRedisService.createOne(categoryMapper.toCategoryDocument(category));
    }

    public void createAll(List<CategoryDTO> categories) {
        categoryRedisService.createAll(categoryMapper.toCategoryDocument(categories));
    }

    public void updateOne(CategoryDTO category) {
        categoryRedisService.updateOne(categoryMapper.toCategoryDocument(category));
    }

    public void updateAll(List<CategoryDTO> categories) {
        categoryRedisService.updateAll(categoryMapper.toCategoryDocument(categories));
    }

    public void deleteOne(Long id) {
        categoryRedisService.deleteOne(id);
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisService.deleteAll(ids);
    }
}