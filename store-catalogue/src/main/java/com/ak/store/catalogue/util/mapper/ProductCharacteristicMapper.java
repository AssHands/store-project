package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.dto.ProductCharacteristicDTOnew;
import com.ak.store.catalogue.model.dto.write.ProductCharacteristicWriteDTO;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.common.model.catalogue.snapshot.ProductCharacteristicSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductCharacteristicMapper {
    @Mapping(target = "characteristicId", source = "characteristic.id")
    @Mapping(target = "productId", source = "product.id")
    ProductCharacteristicDTOnew toProductCharacteristicDTOnew(ProductCharacteristic pc);
    List<ProductCharacteristicDTOnew> toProductCharacteristicDTOnew(List<ProductCharacteristic> pc);

    @Mapping(target = "characteristic.id", source = "pc.characteristicId")
    @Mapping(target = "product.id", source = "productId")
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicWriteDTO pc, Long productId);
    List<ProductCharacteristic> toProductCharacteristic(List<ProductCharacteristicWriteDTO> pc, Long productId);

    ProductCharacteristicSnapshot toProductCharacteristicSnapshot(ProductCharacteristicDTOnew pc);
    List<ProductCharacteristicSnapshot> toProductCharacteristicSnapshot(List<ProductCharacteristicDTOnew> pc);
}
