package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryValidator {
    private final CategoryRepo categoryRepo;

    public void validateCreate(WriteCategoryCommand command) {
        uniqName(command.getName());
        existParent(command.getId(), command.getParentId());
    }

    public void validateUpdate(WriteCategoryCommand command) {
        uniqName(command.getName());
        existParent(command.getId(), command.getParentId());
    }

    //todo сделать проверку, что продуктов с этой категорией не существует
    public void validateDelete(Long id) {
        notExistChildren(id);
    }

    public void validateRemoveOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        categoryContainsCharacteristic(command.getCategoryId(), command.getCharacteristicId());
    }

    //todo проверять, что характеристика с таким id существует в бд
    public void validateAddOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        categoryNotContainsCharacteristic(command.getCategoryId(), command.getCharacteristicId());
    }

    private void uniqName(String name) {
        if (categoryRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private void existParent(Long id, Long parentId) {
        if(parentId == null) return;

        if (id != null && id.equals(parentId)) {
            throw new IllegalArgumentException("parentId cannot be equal to id");
        }

        categoryRepo.findById(parentId).orElseThrow(() -> new RuntimeException("parent does not exist"));
    }

    private void notExistChildren(Long id) {
        if (categoryRepo.existsByParentId(id)) {
            throw new RuntimeException("this category has children. delete children first");
        }
    }

    private void categoryContainsCharacteristic(Long categoryId, Long characteristicId) {
        var category = findOneFullById(categoryId);

        var isContains = category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));

        if(!isContains) {
            throw new RuntimeException("category does not contain characteristic");
        }
    }

    private void categoryNotContainsCharacteristic(Long categoryId, Long characteristicId) {
        var category = findOneFullById(categoryId);

        var isContains = category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));

        if(isContains) {
            throw new RuntimeException("category contains characteristic");
        }
    }

    private Category findOneFullById(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
