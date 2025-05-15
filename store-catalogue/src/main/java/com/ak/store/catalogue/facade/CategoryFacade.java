package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.util.mapper.CategoryMapper;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final OutboxTaskService<CategorySnapshotPayload> outboxTaskService;

    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }

    @Transactional
    public Long createOne(CategoryWriteDTO request) {
        var category = categoryService.createOne(request);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(Collections.emptyList())
                .relatedCategories(Collections.emptyList())
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_CREATED);
        return category.getId();
    }

    @Transactional
    public Long updateOne(Long id, CategoryWriteDTO request) {
        var category = categoryService.updateOne(id, request);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var category = categoryService.deleteOne(id);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(Collections.emptyList())
                .relatedCategories(Collections.emptyList())
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_DELETED);
    }

    @Transactional
    public Long addCharacteristic(Long categoryId, Long characteristicId) {
        var category = categoryService.addCharacteristic(categoryId, characteristicId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long removeCharacteristic(Long categoryId, Long characteristicId) {
        var category = categoryService.removeCharacteristic(categoryId, characteristicId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long addRelatedCategory(Long categoryId, Long relatedId) {
        var category = categoryService.addRelatedCategory(categoryId, relatedId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long removeRelatedFromCategory(Long categoryId, Long relatedId) {
        var category = categoryService.removeRelatedCategory(categoryId, relatedId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CATEGORY_UPDATED);
        return category.getId();
    }
}
