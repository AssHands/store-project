package com.ak.store.common.payload.search;

import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchAvailableFiltersRequest {

    private Long categoryId;

    private int priceFrom;

    @Min(1)
    private int priceTo;

    private String text;

    @Valid
    private Filters filters = new Filters();

    public List<NumericFilter> getNumericFilters() {
        return filters.getNumericFilters();
    }

    public List<TextFilter> getTextFilters() {
        return filters.getTextFilters();
    }
}