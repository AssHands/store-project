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
    private final CategoryRedisService categoryRedisService;

    public void createAll(List<CategoryDTO> categories) {
        categoryRedisService.createAll(categories);
    }

    public void updateAll(List<CategoryDTO> categories) {
        categoryRedisService.updateAll(categories);
    }

    public void deleteAll(List<Long> ids) {
        categoryRedisService.deleteAll(ids);
    }
}