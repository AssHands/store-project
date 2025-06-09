package com.ak.store.catalogueRatingUpdater.mapper;

import com.ak.store.catalogueRatingUpdater.model.dto.ProductRatingDTO;
import com.ak.store.catalogueRatingUpdater.model.entity.Product;
import com.ak.store.common.snapshot.catalogue.ProductRatingUpdatedSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductRatingDTO toProductRatingDTO(Product p);

    ProductRatingUpdatedSnapshot toProductRatingUpdatedSnapshot(ProductRatingDTO pr);
}
