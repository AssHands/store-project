package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CategoryServiceValidator {
    private final CategoryRepo categoryRepo;

    public void validateCreating(CategoryWriteDTO request) {
        checkUniqName(request.getName());

        if (request.getParentId() != null) {
            findOne(request.getParentId());
        }
    }

    public void validateUpdating(Long id, CategoryWriteDTO request) {
        var category = findOne(id);

        if (request.getParentId() != null && Objects.equals(category.getId(), request.getParentId())) {
            throw new RuntimeException("must category_id != parent_id");
        }

        checkUniqName(request.getName());

        if (request.getParentId() != null && !Objects.equals(category.getParentId(), request.getParentId())) {
            findOne(request.getParentId());
        }
    }

    public void validateDeleting(Long id) {
        if (categoryRepo.existsByParentId(id)) {
            throw new RuntimeException("this category has children. delete children first");
        }
    }

    public void validateRemovingCharacteristic(Long id, Long characteristicId) {
        var category = findOneWithCharacteristics(id);

        if (!isCategoryContainsCharacteristic(category, characteristicId)) {
            throw new RuntimeException("characteristic is not bound to category");
        }
    }

    public void validateAddingCharacteristic(Long id, Long characteristicId) {
        var category = findOneWithCharacteristics(id);

        if (isCategoryContainsCharacteristic(category, characteristicId)) {
            throw new RuntimeException("characteristic is already bound to category");
        }
    }

    public void validateRemovingRelatedCategory(Long id, Long relatedId) {
        var category = findOneWithRelatedCategories(id);
        boolean isRelatedExist = isCategoryContainsRelatedCategory(category, relatedId);

        if (!isRelatedExist) {
            throw new RuntimeException("related with id=%d is not bound to category".formatted(relatedId));
        }
    }

    public void validateAddingRelatedCategory(Long id, Long relatedId) {
        if (id.equals(relatedId)) {
            throw new RuntimeException("id and related id must not be equal");
        }

        var category = findOneWithRelatedCategories(id);
        boolean isRelatedExist = isCategoryContainsRelatedCategory(category, relatedId);

        if (isRelatedExist) {
            throw new RuntimeException("this related is already bound to category");
        }
    }

    private void checkUniqName(String name) {
        if (categoryRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private boolean isCategoryContainsRelatedCategory(Category category, Long relatedId) {
        return category.getRelatedCategories().stream()
                .map(Category::getId)
                .anyMatch(v -> v.equals(relatedId));
    }

    private boolean isCategoryContainsCharacteristic(Category category, Long characteristicId) {
        return category.getCharacteristics().stream()
                .map(CategoryCharacteristic::getCharacteristic)
                .map(Characteristic::getId)
                .anyMatch(v -> v.equals(characteristicId));
    }

    private Category findOne(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Category findOneWithRelatedCategories(Long id) {
        return categoryRepo.findOneWithRelatedCategoriesById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Category findOneWithCharacteristics(Long id) {
        return categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
