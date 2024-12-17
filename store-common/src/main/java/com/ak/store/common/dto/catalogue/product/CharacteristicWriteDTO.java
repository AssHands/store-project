package com.ak.store.common.dto.catalogue.product;

import com.ak.store.common.dto.catalogue.others.nested.NumericCharacteristic;
import com.ak.store.common.dto.catalogue.others.nested.TextCharacteristic;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicWriteDTO {
    private Long characteristicId;

    private Integer numericValue;

    private String textValue;
}
