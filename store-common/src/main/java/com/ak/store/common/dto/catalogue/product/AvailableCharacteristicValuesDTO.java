package com.ak.store.common.dto.catalogue.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailableCharacteristicValuesDTO {
    private Long characteristicId;

    private String name;

    private List<String> textValues = new ArrayList<>();

    public AvailableCharacteristicValuesDTO(Long characteristicId, String name) {
        this.characteristicId = characteristicId;
        this.name = name;
    }
}