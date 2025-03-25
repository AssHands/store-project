package com.ak.store.search.util.mapper;

import com.ak.store.common.model.catalogue.document.ProductDocument;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductPoorView toProductPoorView(ProductDocument productDocument);
}