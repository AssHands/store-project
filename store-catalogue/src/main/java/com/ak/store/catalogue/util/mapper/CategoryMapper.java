package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.common.model.catalogue.view.CategoryTreeView;
import com.ak.store.common.model.catalogue.view.CategoryView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {

    CategoryView toCategoryView(Category category);

    CategoryTreeView toCategoryTreeView(Category category);
}
