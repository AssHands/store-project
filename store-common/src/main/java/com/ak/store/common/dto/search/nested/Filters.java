package com.ak.store.common.dto.search.nested;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Filters {
    @JsonProperty("numeric_filters")
    private List<NumericFilter> numericFilters;

    @JsonProperty("text_filters")
    private List<TextFilter> textFilters;
}