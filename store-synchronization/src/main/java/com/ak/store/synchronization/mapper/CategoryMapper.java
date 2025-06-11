package com.ak.store.synchronization.mapper;

import com.ak.store.synchronization.model.document.Category;
import com.ak.store.common.snapshot.catalogue.CategorySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    Category toCategoryDocument(CategorySnapshot cs);
    List<Category> toCategoryDocument(List<CategorySnapshot> cs);
}