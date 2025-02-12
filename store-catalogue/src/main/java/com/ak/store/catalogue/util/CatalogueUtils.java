package com.ak.store.catalogue.util;

import com.ak.store.common.model.catalogue.view.CategoryView;

import java.util.*;

public abstract class CatalogueUtils {

    static public List<CategoryView> buildCategoryTree(List<CategoryView> categories) {
        if(categories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CategoryView> categoryMap = new LinkedHashMap<>();
        List<CategoryView> rootCategories = new ArrayList<>();

        for (CategoryView category : categories) {
            categoryMap.put(category.getId(), category);
        }

        for (CategoryView category : categories) {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                CategoryView parent = categoryMap.get(category.getParentId());
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