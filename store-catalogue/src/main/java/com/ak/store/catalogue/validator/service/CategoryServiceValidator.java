package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryServiceValidator {
    //todo поменять на сервис
    private final CategoryRepo categoryRepo;

    public void validateCreating(CategoryWriteDTO category) {
        checkUniqName(category.getName());

        if (category.getParentId() != null) {
            checkParentExist(category.getParentId());
        }
    }

    public void validateUpdating(CategoryDTO category, CategoryWriteDTO request) {
        if (request.getParentId() != null && category.getId().equals(request.getParentId())) {
            throw new RuntimeException("must category_id != parent_id");
        }

        checkUniqName(request.getName());

        if (request.getParentId() != null && !category.getParentId().equals(request.getParentId())) {
            checkParentExist(request.getParentId());
        }
    }

    public void validateDeleting(Long id) {
        if (categoryRepo.existsByParentId(id)) {
            throw new RuntimeException("this category has children. delete children first");
        }
    }

    public void validateRemovingCharacteristic(List<Long> existingCharacteristics, Long characteristicId) {
        if(!existingCharacteristics.contains(characteristicId)) {
            throw new RuntimeException("characteristic is not bound to category");
        }
    }

    public void validateAddingCharacteristic(List<Long> existingCharacteristics, Long characteristicId) {
        if(existingCharacteristics.contains(characteristicId)) {
            throw new RuntimeException("characteristic is already bound to category");
        }
    }

    public void validateRemovingRelatedCategory(List<Long> existingRelatedCategories, Long relatedId) {
        boolean isRelatedExist = isRelatedExist(existingRelatedCategories, relatedId);

        if (!isRelatedExist) {
            throw new RuntimeException("related with id=%d is not bound to category".formatted(relatedId));
        }
    }

    public void validateAddingRelatedCategory(Long id, Long relatedId, List<Long> existingRelatedCategories) {
        if(id.equals(relatedId)) {
            throw new RuntimeException("id and related id must not be equal");
        }

        boolean isRelatedExist = isRelatedExist(existingRelatedCategories, relatedId);

        if (isRelatedExist) {
            throw new RuntimeException("this related is already bound to category");
        }
    }

    private void checkUniqName(String name) {
        // todo если имя отправляется такое же, то будет ошибка
        if (categoryRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private void checkParentExist(Long parentId) {
        categoryRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("parent is not exist"));
    }

    private boolean isRelatedExist(List<Long> existingRelatedCategories, Long relatedId) {
        for (var related : existingRelatedCategories) {
            if (related.equals(relatedId)) {
                return true;
            }
        }
        return false;
    }
}
