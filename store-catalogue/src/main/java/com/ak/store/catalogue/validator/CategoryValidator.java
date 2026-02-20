package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.exception.ConflictException;
import com.ak.store.catalogue.exception.ValidationException;
import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.service.CategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryValidator {
    private final CategoryQueryService categoryQueryService;

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
        if (categoryQueryService.existsByName(name)) {
            throw new ConflictException("Category name must be unique");
        }
    }

    private void existParent(Long id, Long parentId) {
        if(parentId == null) return;

        if (id != null && id.equals(parentId)) {
            throw new IllegalArgumentException("parentId cannot be equal to id");
        }

        categoryQueryService.findByIdOrThrow(parentId);
    }

    private void notExistChildren(Long id) {
        if (categoryQueryService.existsChildren(id)) {
            throw new ConflictException("Category has child categories. Delete child categories first");
        }
    }

    private void categoryContainsCharacteristic(Long categoryId, Long characteristicId) {
        var category = findOneFullById(categoryId);

        var isContains = category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));

        if(!isContains) {
            throw new ValidationException("Category does not contain characteristic");
        }
    }

    private void categoryNotContainsCharacteristic(Long categoryId, Long characteristicId) {
        var category = findOneFullById(categoryId);

        var isContains = category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));

        if(isContains) {
            throw new ConflictException("Category already contains this characteristic");
        }
    }

    private Category findOneFullById(Long id) {
        return categoryQueryService.findOneFullByIdOrThrow(id);
    }
}
