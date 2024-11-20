package com.ak.store.common.dto.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayload {
    @Min(1)
    @Max(100)
    private int limit = 20;

    @JsonProperty("search_after")
    private List<Integer> searchAfter;

    @Max(Integer.MAX_VALUE)
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

    @Getter(AccessLevel.NONE)
    private Filter filters;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private Sort sort;

    public List<NumericFilter> getNumericFilters() {
        if(filters == null) {
            return null;
        }
        return filters.getNumericFilters();
    }

    public List<TextFilter> getTextFilters() {
        if(filters == null) {
            return null;
        }
        return filters.getTextFilters();
    }

    public Filter getAllFilters() {
        return filters;
    }
}