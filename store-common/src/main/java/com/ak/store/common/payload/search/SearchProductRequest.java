package com.ak.store.common.payload.search;

import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.Sort;
import com.ak.store.common.dto.search.nested.TextFilter;
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
    private Filters filters = new Filters();

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private Sort sort = Sort.POPULAR;

    public List<NumericFilter> getNumericFilters() {
        return filters.getNumericFilters();
    }

    public List<TextFilter> getTextFilters() {
        return filters.getTextFilters();
    }

    public Filters getAllFilters() {
        return filters;
    }


}