package com.ak.store.common.dto.catalogue.others;

import com.ak.store.common.dto.catalogue.others.nested.NumericCharacteristic;
import com.ak.store.common.dto.catalogue.others.nested.TextCharacteristic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicsWriteDTO {

    @JsonProperty("text_characteristics")
    List<TextCharacteristic> textCharacteristics = new ArrayList<>();

    @JsonProperty("numeric_characteristics")
    List<NumericCharacteristic> numericCharacteristics = new ArrayList<>();
}
