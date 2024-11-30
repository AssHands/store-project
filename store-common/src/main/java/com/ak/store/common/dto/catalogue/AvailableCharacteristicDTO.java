package com.ak.store.common.dto.catalogue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "characteristicId", "name", "textValues" })
public class AvailableCharacteristicDTO {
    @JsonProperty("characteristic_id")
    private Long characteristicId;

    private String name;

    @JsonProperty("text_values")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> textValues;

    public AvailableCharacteristicDTO(Long characteristicId, String name) {
        this.characteristicId = characteristicId;
        this.name = name;
    }
}