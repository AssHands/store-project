package com.ak.store.common.dto.catalogue.others.nested;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextCharacteristic {

    @NotNull
    @JsonProperty("characteristic_id")
    private Long characteristicId;

    @NotNull
    private String value;
}
