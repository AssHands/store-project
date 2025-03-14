package com.ak.store.synchronization.util.mapper;

import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductDocument toProductDocument(ProductDTO productDTO);
}
