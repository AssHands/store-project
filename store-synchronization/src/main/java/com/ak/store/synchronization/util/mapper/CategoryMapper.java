package com.ak.store.synchronization.util.mapper;

import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.synchronization.model.document.CategoryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    CategoryDocument toCategoryDocument(CategoryDTO category);
    List<CategoryDocument> toCategoryDocument(List<CategoryDTO> categories);
}
