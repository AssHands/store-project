package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.util.CatalogueMapper0;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import com.ak.store.common.model.catalogue.view.CategoryView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceFacade {
    private final CategoryService categoryService;
    private final CatalogueMapper0 catalogueMapper0;

    public List<CategoryView> findAll() {
        return CatalogueUtils.buildCategoryTree(categoryService.findAll().stream()
                .map(catalogueMapper0::mapToCategoryView)
                .toList());
    }

    @Transactional
    public Long createOne(CategoryForm categoryForm) {
        return categoryService.createOne(categoryForm).getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryService.deleteOne(id);
    }

    @Transactional
    public Long updateOne(Long id, CategoryForm categoryForm) {
        return categoryService.updateOne(id, categoryForm).getId();
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
