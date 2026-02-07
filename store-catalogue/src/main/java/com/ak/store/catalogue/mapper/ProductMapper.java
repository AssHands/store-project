package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.model.command.WriteProductCommand;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.form.WriteProductForm;
import com.ak.store.catalogue.model.view.ProductView;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDTO(Product entity);

    ProductView toView(ProductDTO dto);

    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(WriteProductCommand command);

    WriteProductCommand toWriteCommand(WriteProductForm form);

    @Mapping(target = "categoryId", source = "category.id")
    ProductSnapshot toSnapshot(Product entity);
}
