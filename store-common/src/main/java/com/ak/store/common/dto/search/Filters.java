package com.ak.store.common.dto.search;

import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY) //todo: it works?
public class Filters {
    @JsonProperty("numeric_filters")
    //@JsonInclude(JsonInclude.Include.NON_EMPTY) todo: check for errors if list will be initialized
    private List<NumericFilter> numericFilters = new ArrayList<>();

    @JsonProperty("text_filters")
    private List<TextFilter> textFilters = new ArrayList<>();
}