package com.ak.store.common.payload.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAvailableFilters {
    @NotNull
    @JsonProperty(required = true)
    private Long categoryId;

    @NotNull
    @NotBlank
    @JsonProperty(required = true)
    private String text;
}
