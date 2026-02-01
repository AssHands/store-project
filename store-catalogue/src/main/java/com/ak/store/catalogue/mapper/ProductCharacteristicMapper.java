package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductCharacteristicMapper {
    @Mapping(target = "characteristicId", source = "characteristic.id")
    @Mapping(target = "productId", source = "product.id")
    ProductCharacteristicDTO toDTO(ProductCharacteristic entity);

    @Mapping(target = "characteristic.id", source = "pc.characteristicId")
    @Mapping(target = "product.id", source = "productId")
    ProductCharacteristic toEntity(WriteProductCharacteristicCommand command, Long productId);

    @Mapping(target = "id", source = "characteristicId")
    ProductCharacteristicSnapshot toSnapshot(ProductCharacteristicDTO dto);
}