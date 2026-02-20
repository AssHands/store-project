package com.ak.store.catalogue.category.facade;

import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.category.service.CategoryOutboxService;
import com.ak.store.catalogue.category.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {
    private final CategoryService categoryService;
    private final CategoryOutboxService categoryOutboxService;

    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }

    @Transactional
    public Long createOne(WriteCategoryCommand command) {
        var category = categoryService.createOne(command);
        categoryOutboxService.saveCreatedEvent(category.getId());
        return category.getId();
    }

    @Transactional
    public Long updateOne(WriteCategoryCommand command) {
        var category = categoryService.updateOne(command);
        categoryOutboxService.saveUpdatedEvent(category.getId());
        return category.getId();
    }

    @Transactional
    public Long deleteOne(Long id) {
        categoryService.deleteOne(id);
        categoryOutboxService.saveDeletedEvent(id);
        return id;
    }

    @Transactional
    public Long addOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = categoryService.addOneCharacteristic(command);
        categoryOutboxService.saveUpdatedEvent(category.getId());
        return category.getId();
    }

    @Transactional
    public Long removeOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = categoryService.removeOneCharacteristic(command);
        categoryOutboxService.saveUpdatedEvent(category.getId());
        return category.getId();
    }
}
