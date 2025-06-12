package com.ak.store.synchronization.mapper;


import com.ak.store.synchronization.model.document.Product;
import com.ak.store.common.snapshot.catalogue.ProductRatingUpdatedSnapshot;
import com.ak.store.common.snapshot.catalogue.ProductSnapshotPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    @Mapping(source = "product", target = ".")
    @Mapping(source = "productCharacteristics", target = "characteristics")
    @Mapping(source = "images", target = "images")
    Product toProduct(ProductSnapshotPayload psp);

    Product toProduct(ProductRatingUpdatedSnapshot prus);
}