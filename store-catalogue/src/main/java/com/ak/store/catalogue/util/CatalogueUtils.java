package com.ak.store.catalogue.util;

import com.ak.store.common.dto.catalogue.CategoryDTO;

import java.util.*;

public abstract class CatalogueUtils {

    static public List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
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