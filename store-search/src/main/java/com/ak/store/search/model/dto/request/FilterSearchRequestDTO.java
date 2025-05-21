package com.ak.store.search.model.dto.request;

import com.ak.store.search.model.dto.FiltersDTO;
import com.ak.store.search.model.dto.NumericFilterDTO;
import com.ak.store.search.model.dto.TextFilterDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterSearchRequestDTO {
    private Long categoryId;

    private Integer priceFrom;

    private Integer priceTo;

    private String text;

    private FiltersDTO filters = new FiltersDTO();

    public List<NumericFilterDTO> getNumericFilters() {
        return filters.getNumericFilters();
    }

    public List<TextFilterDTO> getTextFilters() {
        return filters.getTextFilters();
    }
}