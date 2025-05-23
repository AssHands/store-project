package com.ak.store.synchronization.mapper;


import com.ak.store.common.model.catalogue.document.ProductDocument;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    @Mapping(source = "product", target = ".")
    @Mapping(source = "productCharacteristics", target = "characteristics")
    @Mapping(source = "images", target = "images")
    ProductDocument toProductDocument(ProductSnapshotPayload psp);

    List<ProductDocument> toProductDocument(List<ProductSnapshotPayload> psp);
}