package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.catalogue.util.mapper.CategoryMapper;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import com.ak.store.common.model.catalogue.view.CategoryTreeView;
import com.ak.store.common.model.catalogue.view.CategoryView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final OutboxTaskService<CategoryDTO> outboxTaskService;

    public List<CategoryView> findAll() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toCategoryView)
                .toList();
    }

    public List<CategoryTreeView> findAllAsTree() {
        return CatalogueUtils.buildCategoryTree(categoryService.findAll().stream()
                .map(categoryMapper::toCategoryTreeView)
                .toList());
    }

    @Transactional
    public Long createOne(CategoryForm categoryForm) {
        var category = categoryService.createOne(categoryForm);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_CREATED);
        return category.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var category = categoryService.deleteOne(id);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_DELETED);
    }

    @Transactional
    public Long updateOne(Long id, CategoryForm categoryForm) {
        var category = categoryService.updateOne(id, categoryForm);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long addCharacteristicToCategory(Long categoryId, Long characteristicId) {
        var category = categoryService.addCharacteristicToCategory(categoryId, characteristicId);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        var category = categoryService.deleteCharacteristicFromCategory(categoryId, characteristicId);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long addRelatedToCategory(Long categoryId, Long relatedId) {
        var category = categoryService.addRelatedToCategory(categoryId, relatedId);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long deleteRelatedFromCategory(Long categoryId, Long relatedId) {
        var category = categoryService.deleteRelatedFromCategory(categoryId, relatedId);
        outboxTaskService.createOneTask(categoryMapper.toCategoryDTO(category), OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }
}
