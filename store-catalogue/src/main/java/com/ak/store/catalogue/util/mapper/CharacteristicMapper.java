package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.RangeValueDTO;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)

public interface CharacteristicMapper {

    @Mapping(target = "textValues", source = "textValues")
    CharacteristicView toCharacteristicView(Characteristic characteristic);

    Characteristic toCharacteristic(CharacteristicForm characteristicForm);

    @Mapping(target = "fromValue", source = "rangeValueForm.from")
    @Mapping(target = "toValue", source = "rangeValueForm.to")
    @Mapping(target = "characteristic", expression = "java(characteristic)")
    @Mapping(target = "id", ignore = true)
    RangeValue toRangeValue(RangeValueForm rangeValueForm, Characteristic characteristic);

    @Mapping(target = "textValue", source = "textValueForm.text")
    @Mapping(target = "characteristic", expression = "java(characteristic)")
    @Mapping(target = "id", ignore = true)
    TextValue toTextValue(TextValueForm textValueForm, Characteristic characteristic);

    @Mapping(target = "product", expression = "java(product)")
    @Mapping(target = "characteristic", expression = "java(characteristic)")
    @Mapping(target = "id", ignore = true)
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicForm productCharacteristicForm,
                                                  Characteristic characteristic, Product product);

    CharacteristicDTO toCharacteristicDTO(Characteristic characteristic);

    default List<String> mapTextValues(List<TextValue> textValues) {
        if (textValues == null) {
            return Collections.emptyList();
        }
        return textValues.stream()
                .map(TextValue::getTextValue)
                .toList();
    }

    default List<RangeValueDTO> mapRangeValues(List<RangeValue> rangeValues) {
        if (rangeValues == null) {
            return Collections.emptyList();
        }

        List<RangeValueDTO> rangeValueDTOs = new ArrayList<>();
        for (RangeValue rangeValue : rangeValues) {
            rangeValueDTOs.add(RangeValueDTO.builder()
                    .to(rangeValue.getToValue())
                    .from(rangeValue.getFromValue())
                    .build());
        }
        return rangeValueDTOs;
    }
}
