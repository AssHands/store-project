package com.ak.store.common.payload.search;

import com.ak.store.common.model.search.dto.FiltersDTO;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.SortingType;
import com.ak.store.common.model.search.common.TextFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchProductRequest {
    @Min(1)
    @Max(100)
    private int limit = 20;

    private List<Integer> searchAfter = new ArrayList<>();

    private int priceFrom;

    @Min(0)
    private int priceTo;

    private String text;

    private Long categoryId;

    @Valid
    private FiltersDTO filtersDTO = new FiltersDTO();

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private SortingType sortingType = SortingType.POPULAR;

    public List<NumericFilter> getNumericFilters() {
        return filtersDTO.getNumericFilters();
    }

    public List<TextFilter> getTextFilters() {
        return filtersDTO.getTextFilters();
    }

    public FiltersDTO getAllFilters() {
        return filtersDTO;
    }


}