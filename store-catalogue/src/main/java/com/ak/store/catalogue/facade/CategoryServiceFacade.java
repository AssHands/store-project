package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.common.model.catalogue.view.CategoryView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceFacade {
    private final CategoryService categoryService;
    private final CatalogueMapper catalogueMapper;

    public List<CategoryView> findAll() {
        return CatalogueUtils.buildCategoryTree(categoryService.findAll().stream()
                .map(catalogueMapper::mapToCategoryView)
                .toList());
    }

    @Transactional
    public Long createOne(CategoryDTO categoryDTO) {
        return categoryService.createOne(categoryDTO).getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryService.deleteOne(id);
    }

    @Transactional
    public Long updateOne(Long id, CategoryDTO categoryDTO) {
        return categoryService.updateOne(id, categoryDTO).getId();
    }

    @Transactional
    public Long addCharacteristicToCategory(Long categoryId, Long characteristicId) {
         return categoryService.addCharacteristicToCategory(categoryId, characteristicId).getId();
    }

    @Transactional
    public Long deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        return categoryService.deleteCharacteristicFromCategory(categoryId, characteristicId).getId();
    }
}
