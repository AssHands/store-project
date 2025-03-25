package com.ak.store.synchronization.util.mapper;


import com.ak.store.common.model.catalogue.document.ProductDocument;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductDocument toProductDocument(ProductDTO product);
    List<ProductDocument> toProductDocument(List<ProductDTO> products);
}
