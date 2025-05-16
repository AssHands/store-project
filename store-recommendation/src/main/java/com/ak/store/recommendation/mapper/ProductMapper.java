package com.ak.store.recommendation.mapper;

import com.ak.store.common.model.catalogue.document.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductPoorView toProductPoorView(ProductDocument productDocument);
}