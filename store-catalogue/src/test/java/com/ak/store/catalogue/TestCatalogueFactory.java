package com.ak.store.catalogue;

 import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.common.model.catalogue.view.CategoryView;

import java.util.*;

public class TestCatalogueFactory {
    public static Characteristic createCharacteristic(Long id, String name, boolean isText) {
        return Characteristic.builder()
                .id(id)
                .isText(isText)
                .name(name)
                .build();
    }

    public static Characteristic createCharacteristic(Long id, String name, boolean isText, List<String> textValues) {
        Set<TextValue> textValuesSet = new HashSet<>();
        long index = 1;
        for(String value : textValues) {
            textValuesSet.add(TextValue.builder()
                    .id(index)
                    .textValue(value)
                    .characteristic(Characteristic.builder().id(id).build())
                    .build());

            index++;
        }

        return Characteristic.builder()
                .id(id)
                .isText(isText)
                .name(name)
                .textValues(textValuesSet)
                .build();
    }

    public static CategoryView createCategoryDTO(Long id, String name) {
        return CategoryView.builder().id(id).name(name).build();
    }

    public static CategoryView createCategoryDTO(Long id, String name, Long parentId) {
        return CategoryView.builder().id(id).name(name).parentId(parentId).build();
    }

    public static CategoryView createCategoryDTO(Long id, String name, List<CategoryView> childList) {
        return CategoryView.builder().id(id).name(name).childCategories(childList).build();
    }

    public static CategoryView createCategoryDTO(Long id, String name, Long parentId, List<CategoryView> childList) {
        return CategoryView.builder().id(id).name(name).parentId(parentId).childCategories(childList).build();
    }
}