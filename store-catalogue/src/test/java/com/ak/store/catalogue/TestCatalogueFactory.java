package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.RangeValue;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.NumericFilterValue;
import com.ak.store.common.dto.search.nested.TextFilter;

import java.util.*;

public class TestCatalogueFactory {
    public static Characteristic createCharacteristic(Long id, String name, boolean isText, Long categoryId, Map<Integer, Integer> rangeValues) {
        Set<RangeValue> rangeValuesSet = new HashSet<>();
        long index = 1;
        for(Map.Entry<Integer, Integer> entry : rangeValues.entrySet()) {
            rangeValuesSet.add(RangeValue.builder()
                    .id(index)
                    .fromValue(entry.getKey())
                    .toValue(entry.getValue())
                    .characteristic(Characteristic.builder().id(id).build())
                    .build());

            index++;
        }

        return Characteristic.builder()
                .id(id)
                .isText(isText)
                .category(Set.of(Category.builder().id(categoryId).build()))
                .name(name)
                .rangeValues(rangeValuesSet)
                .build();
    }

    public static Characteristic createCharacteristic(Long id, String name, boolean isText, Long categoryId, List<String> textValues) {
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
                .category(Set.of(Category.builder().id(categoryId).build()))
                .name(name)
                .textValues(textValuesSet)
                .build();
    }

    public static Filters createTextFilters(List<TextFilter> textFilters) {
        return Filters.builder().textFilters(textFilters).build();
    }

    public static Filters createNumericFilters(List<NumericFilter> numericFilters) {
        return Filters.builder().numericFilters(numericFilters).build();
    }

    public static Filters createBothFilters(List<TextFilter> textFilters, List<NumericFilter> numericFilters) {
        return Filters.builder().textFilters(textFilters).numericFilters(numericFilters).build();
    }

    public static TextFilter createTextFilter(Long id, String name, List<String> values) {
        return TextFilter.builder()
                .id(id)
                .name(name)
                .values(values)
                .build();
    }

    public static NumericFilter createNumericFilter(Long id, String name, Map<Integer, Integer> numericValues) {
        List<NumericFilterValue> numericFilterValues = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : numericValues.entrySet()) {
            numericFilterValues.add(NumericFilterValue.builder()
                    .from(entry.getKey())
                    .to(entry.getValue())
                    .build());
        }

        return NumericFilter.builder()
                .id(id)
                .name(name)
                .values(numericFilterValues)
                .build();
    }

    public static Category createCategory(Long id, String name) {
        return Category.builder().id(id).name(name).build();
    }

    public static Category createCategory(Long id, String name, Long parentId) {
        return Category.builder().id(id).name(name).parentId(parentId).build();
    }

    public static CategoryDTO createCategoryDTO(Long id, String name) {
        return CategoryDTO.builder().id(id).name(name).build();
    }

    public static CategoryDTO createCategoryDTO(Long id, String name, Long parentId) {
        return CategoryDTO.builder().id(id).name(name).parentId(parentId).build();
    }

    public static CategoryDTO createCategoryDTO(Long id, String name, List<CategoryDTO> childList) {
        return CategoryDTO.builder().id(id).name(name).childCategories(childList).build();
    }

    public static CategoryDTO createCategoryDTO(Long id, String name, Long parentId, List<CategoryDTO> childList) {
        return CategoryDTO.builder().id(id).name(name).parentId(parentId).childCategories(childList).build();
    }
}