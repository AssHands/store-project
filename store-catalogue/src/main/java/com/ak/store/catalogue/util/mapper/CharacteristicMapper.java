package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)

public interface CharacteristicMapper {

    @Mapping(target = "textValues", source = "textValues")
    CharacteristicView toCharacteristicView(Characteristic characteristic);

    default List<String> mapTextValues(List<TextValue> textValues) {
        if (textValues == null) {
            return Collections.emptyList();
        }
        return textValues.stream()
                .map(TextValue::getTextValue)
                .toList();
    }
}
