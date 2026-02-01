package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.mapper.CategoryMapper;
import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Long createOne(WriteCategoryCommand command) {
        var category = categoryService.createOne(command);

//        var snapshot = CategorySnapshotPayload.builder()
//                .category(categoryMapper.toSnapshot(category))
//                .characteristics(Collections.emptyList())
//                .relatedCategories(Collections.emptyList())
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_CREATED);
        return category.getId();
    }

    @Transactional
    public Long updateOne(WriteCategoryCommand command) {
        var category = categoryService.updateOne(command);

//        var snapshot = CategorySnapshotPayload.builder()
//                .category(categoryMapper.toSnapshot(category))
//                .characteristics(categoryService.findAllCharacteristic(category.getId()))
//                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var category = categoryService.deleteOne(id);

        var snapshot = category.getId().toString();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_DELETED);
    }

    @Transactional
    public Long addOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = categoryService.addOneCharacteristic(command);

//        var snapshot = CategorySnapshotPayload.builder()
//                .category(categoryMapper.toSnapshot(category))
//                .characteristics(categoryService.findAllCharacteristic(category.getId()))
//                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }

    @Transactional
    public Long removeOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = categoryService.removeOneCharacteristic(command);

//        var snapshot = CategorySnapshotPayload.builder()
//                .category(categoryMapper.toSnapshot(category))
//                .characteristics(categoryService.findAllCharacteristic(category.getId()))
//                .relatedCategories(categoryService.findAllRelatedCategory(category.getId()))
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
        return category.getId();
    }
}
