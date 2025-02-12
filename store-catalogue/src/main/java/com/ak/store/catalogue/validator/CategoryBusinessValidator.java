package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryBusinessValidator {
    private final CategoryRepo categoryRepo;

    public void validateCreation(CategoryDTO categoryDTO) {
        checkUniqName(categoryDTO.getName());
        checkParentExist(categoryDTO.getParentId());
    }

    public void validateUpdate(Category category, CategoryDTO categoryDTO) {
        if(categoryDTO.getParentId() != null && category.getId().equals(categoryDTO.getParentId())) {
            throw new RuntimeException("must category_id != parent_id");
        }
        checkUniqName(categoryDTO.getName());
        checkParentExist(categoryDTO.getParentId());
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
