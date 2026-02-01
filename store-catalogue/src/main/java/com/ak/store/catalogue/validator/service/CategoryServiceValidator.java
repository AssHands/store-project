package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryServiceValidator {
    private final CategoryRepo categoryRepo;

    public void validateCreate(WriteCategoryCommand command) {
        validateName(command.getName());
        validateParent(command.getId(), command.getParentId());
    }

    public void validateUpdate(WriteCategoryCommand command) {
        validateName(command.getName());
        validateParent(command.getId(), command.getParentId());
    }

    //todo сделать проверку, что продуктов с этой категорией не существует
    public void validateDelete(Long id) {
        validateChildren(id);
    }

    public void validateRemoveCharacteristic(Long id, Long characteristicId) {
        var category = findOneFullById(id);

        if (!isCategoryContainsCharacteristic(category, characteristicId)) {
            throw new RuntimeException("characteristic is not bound to category");
        }
    }

    //todo проверять, что характеристика с таким id существует в бд
    public void validateAddCharacteristic(Long id, Long characteristicId) {
        var category = findOneFullById(id);

        if (isCategoryContainsCharacteristic(category, characteristicId)) {
            throw new RuntimeException("characteristic is already bound to category");
        }
    }

    private void validateName(String name) {
        if (categoryRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private void validateParent(Long id, Long parentId) {
        if(parentId == null) return;

        categoryRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("parent does not exist"));

        if (id.equals(parentId)) {
            throw new RuntimeException("incorrect parentId");
        }
    }

    private void validateChildren(Long id) {
        if (categoryRepo.existsByParentId(id)) {
            throw new RuntimeException("this category has children. delete children first");
        }
    }

    private boolean isCategoryContainsCharacteristic(Category category, Long characteristicId) {
        return category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));
    }

    private Category findOneFullById(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
