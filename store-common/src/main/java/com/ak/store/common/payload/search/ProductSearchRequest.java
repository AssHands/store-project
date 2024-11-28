package com.ak.store.common.payload.search;

import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.Sort;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchRequest {
    @Min(1)
    @Max(100)
    private int limit = 20;

    @JsonProperty("search_after")
    private List<Integer> searchAfter;

    @JsonProperty("price_from")
    private int priceFrom;

    @Min(0)
    @JsonProperty("price_to")
    private int priceTo;

    @NotNull
    @NotBlank
    @JsonProperty(required = true)
    private String text;

    @JsonProperty("category_id")
    private Long categoryId;

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