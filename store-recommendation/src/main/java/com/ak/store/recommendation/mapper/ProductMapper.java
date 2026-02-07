package com.ak.store.recommendation.mapper;

import com.ak.store.recommendation.model.document.Product;
import com.ak.store.recommendation.model.dto.ProductDTO;
import com.ak.store.recommendation.model.view.ProductView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductDTO toDTO(Product document);
}