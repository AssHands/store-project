package com.ak.store.catalogue.validator.business;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryBusinessValidator {
    private final CategoryRepo categoryRepo;

    public void validateCreation(CategoryForm categoryForm) {
        checkUniqName(categoryForm.getName());
        checkParentExist(categoryForm.getParentId());
    }

    public void validateUpdate(Category category, CategoryForm categoryForm) {
        if(categoryForm.getParentId() != null && category.getId().equals(categoryForm.getParentId())) {
            throw new RuntimeException("must category_id != parent_id");
        }
        checkUniqName(categoryForm.getName());
        checkParentExist(categoryForm.getParentId());
    }

    public void validateDeletion(Category category) {
        if(categoryRepo.existsByParentId(category.getParentId())) {
            throw new RuntimeException("this category has children. delete children first");
        }
    }

    public void validateDeleteCharacteristic(Category category, Long characteristicId) {
        boolean isCharacteristicExist = false;
        for (int i = 0; i < category.getCharacteristics().size(); i++) {
            if(category.getCharacteristics().get(i).getCharacteristic().getId().equals(characteristicId)) {
                category.getCharacteristics().remove(i);
                break;
            }
        }

        if(!isCharacteristicExist) {
            throw new RuntimeException("characteristic with id=%d is not bound to category"
                    .formatted(characteristicId));
        }
    }

    public void validateAddCharacteristic(Category category, Long characteristicId) {
        for (CategoryCharacteristic cc : category.getCharacteristics()) {
            if (cc.getCharacteristic().getId().equals(characteristicId)) {
                throw new RuntimeException("this characteristic is already bound to category");
            }
        }
    }

    private void checkUniqName(String name) {
        if(categoryRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private void checkParentExist(Long parentId) {
        categoryRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("parent is not exist"));
    }
}
