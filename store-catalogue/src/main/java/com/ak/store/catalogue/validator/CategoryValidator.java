package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryValidator {
    public void validateCreation(CategoryDTO categoryDTO, List<Category> existCategories) {
        boolean isExistParentId = false;
        for(var category : existCategories) {
            if(category.getName().equals(categoryDTO.getName())) {
                throw new RuntimeException("not uniq name");
            }
            if(categoryDTO.getParentId() != null && category.getId().equals(categoryDTO.getParentId())) {
                isExistParentId = true;
            }
        }

        if(!isExistParentId && categoryDTO.getParentId() != null) {
            throw new RuntimeException("category with id=%d is not exist".formatted(categoryDTO.getParentId()));
        }
    }

    public void validateUpdate(Category category, CategoryDTO categoryDTO, List<Category> existCategories) {
        if(category.getId().equals(categoryDTO.getParentId())) {
            throw new RuntimeException("must category_id != parent_id");
        }

        boolean isExistParentId = false;
        for(var existCategory : existCategories) {
            if(!existCategory.getId().equals(category.getId()) && existCategory.getName().equals(categoryDTO.getName())) {
                throw new RuntimeException("not uniq name");
            }
            if(categoryDTO.getParentId() != null && existCategory.getId().equals(categoryDTO.getParentId())) {
                isExistParentId = true;
            }
        }

        if(!isExistParentId && categoryDTO.getParentId() != null) {
            throw new RuntimeException("category with id=%d is not exist".formatted(categoryDTO.getParentId()));
        }
    }
}
