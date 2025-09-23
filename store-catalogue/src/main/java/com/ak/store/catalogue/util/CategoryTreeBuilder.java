package com.ak.store.catalogue.util;

import com.ak.store.catalogue.model.view.CategoryTreeView;

import java.util.*;

public abstract class CategoryTreeBuilder {

    static public List<CategoryTreeView> build(List<CategoryTreeView> categories) {
        if (categories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CategoryTreeView> categoryMap = new LinkedHashMap<>();
        List<CategoryTreeView> rootCategories = new ArrayList<>();

        for (CategoryTreeView category : categories) {
            categoryMap.put(category.getId(), category);
        }

        for (CategoryTreeView category : categories) {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                CategoryTreeView parent = categoryMap.get(category.getParentId());
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