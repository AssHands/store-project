package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTO;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicCommand;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.model.form.WriteProductCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteProductCharacteristicPayloadForm;
import com.ak.store.catalogue.model.view.ProductCharacteristicView;
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

    ProductCharacteristicView toView(ProductCharacteristicDTO dto);

    WriteProductCharacteristicCommand toWriteCommand(WriteProductCharacteristicForm form);

    WriteProductCharacteristicPayloadCommand toWritePayloadCommand(WriteProductCharacteristicPayloadForm payloadForm);

    @Mapping(target = "id", source = "characteristicId")
    ProductCharacteristicSnapshot toSnapshot(ProductCharacteristicDTO dto);
}