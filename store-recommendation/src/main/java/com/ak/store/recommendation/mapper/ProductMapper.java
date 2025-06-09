package com.ak.store.recommendation.mapper;

import com.ak.store.common.document.catalogue.ProductDocument;
import com.ak.store.recommendation.model.view.ProductView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductView toProductView(ProductDocument productDocument);
}