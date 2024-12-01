package com.ak.store.common.dto.catalogue.others.nested;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumericCharacteristic {

    @JsonProperty("characteristic_id")
    @NotNull
    private Long characteristicId;

    @NotNull
    private Integer value;
}
