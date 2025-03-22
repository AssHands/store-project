package com.ak.store.common.payload.search;

import com.ak.store.common.model.search.dto.FiltersDTO;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.TextFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterSearchRequest {

    private Long categoryId;

    private int priceFrom;

    private int priceTo;

    private String text;

    @Valid
    @JsonProperty("filters")
    private FiltersDTO filtersDTO = new FiltersDTO();

    public List<NumericFilter> getNumericFilters() {
        return filtersDTO.getNumericFilters();
    }

    public List<TextFilter> getTextFilters() {
        return filtersDTO.getTextFilters();
    }
}