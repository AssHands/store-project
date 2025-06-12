package com.ak.store.synchronization.mapper;

import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.model.entity.Characteristic;
import com.ak.store.synchronization.model.entity.NumericValue;
import com.ak.store.synchronization.model.entity.TextValue;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {

    @Mapping(target = "id", source = "characteristic.id")
    @Mapping(target = "name", source = "characteristic.name")
    @Mapping(target = "isText", source = "characteristic.isText")
    @Mapping(target = "textValues", source = "csp", qualifiedByName = "toTextValue")
    @Mapping(target = "numericValues", source = "csp", qualifiedByName = "toNumericValue")
    Characteristic toCharacteristic(CharacteristicSnapshotPayload csp);

    @Named("toTextValue")
    default List<TextValue> toTextValue(CharacteristicSnapshotPayload csp) {
        return csp.getTextValues().stream()
                .map(v -> TextValue.builder()
                        .textValue(v)
                        .characteristic(Characteristic.builder()
                                .id(csp.getCharacteristic().getId())
                                .build())
                        .build())
                .toList();
    }

    @Named("toNumericValue")
    default List<NumericValue> toNumericValue(CharacteristicSnapshotPayload csp) {
        return csp.getNumericValues().stream()
                .map(v -> NumericValue.builder()
                        .fromValue(v.getFromValue())
                        .toValue(v.getToValue())
                        .characteristic(Characteristic.builder()
                                .id(csp.getCharacteristic().getId())
                                .build())
                        .build())
                .toList();
    }
}