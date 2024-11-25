package com.ak.store.common.dto.search.nested;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumericFilterValue {
    @Min(0)
    @JsonProperty(required = true)
    private Integer from;

    @Min(0)
    @JsonProperty(required = true)
    private Integer to;
}
