package com.ak.store.common.dto.search.nested;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "characteristicId", "name","values" })
public class NumericFilter {

    @JsonProperty("characteristic_id")
    private Long characteristicId;

    private String name;

    @NotNull
    @NotBlank
    private List<NumericFilterValue> values = new ArrayList<>();
}