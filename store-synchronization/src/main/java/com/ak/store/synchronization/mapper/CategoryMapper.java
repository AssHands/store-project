package com.ak.store.synchronization.mapper;

import com.ak.store.common.document.catalogue.CategoryDocument;
import com.ak.store.common.snapshot.catalogue.CategorySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    CategoryDocument toCategoryDocument(CategorySnapshot cs);
    List<CategoryDocument> toCategoryDocument(List<CategorySnapshot> cs);
}