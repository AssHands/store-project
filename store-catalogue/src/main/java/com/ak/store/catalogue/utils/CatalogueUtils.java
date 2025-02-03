package com.ak.store.catalogue.utils;

import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CatalogueUtils {

    public List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        if(categories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CategoryDTO> categoryMap = new LinkedHashMap<>();
        List<CategoryDTO> rootCategories = new ArrayList<>();

        for (CategoryDTO category : categories) {
            categoryMap.put(category.getId(), category);
        }

        for (CategoryDTO category : categories) {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                CategoryDTO parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    parent.getChildCategories().add(category);
                } else {
                    throw new RuntimeException("no parent with id " + category.getParentId());
                }
            }
        }

        return rootCategories;
    }
}