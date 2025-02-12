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
    public void createOne(CategoryDTO categoryDTO) {
        categoryService.createOne(categoryDTO);
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryService.deleteOne(id);
    }

    @Transactional
    public void addCharacteristicToCategory(Long categoryId, Long characteristicId) {
        categoryService.addCharacteristicToCategory(categoryId, characteristicId);
    }

    @Transactional
    public void deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        categoryService.deleteCharacteristicFromCategory(categoryId, characteristicId);
    }

    @Transactional
    public void updateOne(Long id, CategoryDTO categoryDTO) {
        categoryService.updateOne(id, categoryDTO);
    }
}
