package com.ak.store.common.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    @JsonProperty("numeric_filters")
    private List<NumericFilter> numericFilters;

    @JsonProperty("text_filters")
    private List<TextFilter> textFilters;
}