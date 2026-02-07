package com.ak.store.synchronization.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.characteristic.CharacteristicPayloadSnapshot;
import com.ak.store.synchronization.model.command.characteristic.WriteCharacteristicPayloadCommand;
import com.ak.store.synchronization.model.entity.Characteristic;
import com.ak.store.synchronization.model.entity.NumericValue;
import com.ak.store.synchronization.model.entity.TextValue;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacteristicMapper {
    WriteCharacteristicPayloadCommand toWritePayloadCommand(CharacteristicPayloadSnapshot snapshot);

    @Mapping(target = "id", source = "characteristic.id")
    @Mapping(target = "name", source = "characteristic.name")
    @Mapping(target = "isText", source = "characteristic.isText")
    @Mapping(target = "textValues", source = "command", qualifiedByName = "toTextValue")
    @Mapping(target = "numericValues", source = "command", qualifiedByName = "toNumericValue")
    Characteristic toDocument(WriteCharacteristicPayloadCommand command);

    @Named("toTextValue")
    default List<TextValue> toTextValue(WriteCharacteristicPayloadCommand command) {
        return command.getTextValues().stream()
                .map(v -> TextValue.builder()
                        .textValue(v)
                        .characteristic(Characteristic.builder()
                                .id(command.getCharacteristic().getId())
                                .build())
                        .build())
                .toList();
    }

    @Named("toNumericValue")
    default List<NumericValue> toNumericValue(WriteCharacteristicPayloadCommand command) {
        return command.getNumericValues().stream()
                .map(v -> NumericValue.builder()
                        .fromValue(v.getFromValue())
                        .toValue(v.getToValue())
                        .characteristic(Characteristic.builder()
                                .id(command.getCharacteristic().getId())
                                .build())
                        .build())
                .toList();
    }
}