package com.ak.store.recommendation.mapper;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.recommendation.model.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductPoorView toProductPoorView(ProductDocument productDocument);
}