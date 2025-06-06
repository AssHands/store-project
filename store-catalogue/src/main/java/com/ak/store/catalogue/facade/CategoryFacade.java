package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.mapper.CategoryMapper;
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
    private final OutboxEventService outboxEventService;

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

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_CREATED);
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

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var category = categoryService.deleteOne(id);

        var snapshot = category.getId().toString();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_DELETED);
    }

    @Transactional
    public Long addOneCharacteristic(Long categoryId, Long characteristicId) {
        var category = categoryService.addOneCharacteristic(categoryId, characteristicId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long removeOneCharacteristic(Long categoryId, Long characteristicId) {
        var category = categoryService.removeOneCharacteristic(categoryId, characteristicId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long addOneRelatedCategory(Long categoryId, Long relatedId) {
        var category = categoryService.addOneRelatedCategory(categoryId, relatedId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long removeOneRelatedFromCategory(Long categoryId, Long relatedId) {
        var category = categoryService.removeOneRelatedCategory(categoryId, relatedId);

        var snapshot = CategorySnapshotPayload.builder()
                .category(categoryMapper.toCategorySnapshot(category))
                .characteristics(categoryService.findAllCharacteristic(category.getId()))
                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }
}
