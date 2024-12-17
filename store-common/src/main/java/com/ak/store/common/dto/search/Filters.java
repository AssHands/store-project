package com.ak.store.common.dto.search;

import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Filters {

    private List<NumericFilter> numericFilters = new ArrayList<>();

    private List<TextFilter> textFilters = new ArrayList<>();
}