package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import com.ak.store.common.model.catalogue.view.CategoryTreeView;
import com.ak.store.common.model.catalogue.view.CategoryView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {

    CategoryView toCategoryView(Category category);

    Category toCategory(CategoryForm categoryForm);

    CategoryTreeView toCategoryTreeView(Category category);

    CategoryDTO toCategoryDTO(Category category);

    default List<Long> mapCategoryCharacteristics(List<CategoryCharacteristic> categoryCharacteristics) {
        if (categoryCharacteristics == null) {
            return Collections.emptyList();
        }

        return categoryCharacteristics.stream()
                .map(cc -> cc.getCharacteristic().getId())
                .toList();
    }

    default List<Long> mapRelatedCategories(Set<Category> relatedCategories) {
        if (relatedCategories == null) {
            return Collections.emptyList();
        }

        return relatedCategories.stream()
                .map(Category::getId)
                .toList();
    }
}
