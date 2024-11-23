package com.ak.store.common.payload.search.nested;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextFilter {
    @JsonProperty("characteristic_id")
    private Long characteristicId;

    @NotNull
    @NotBlank
    private List<String> values;
}