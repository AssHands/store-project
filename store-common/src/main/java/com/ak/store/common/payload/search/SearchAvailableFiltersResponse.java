package com.ak.store.common.payload.search;

import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchAvailableFiltersResponse {
    private Filters filters = new Filters();

    @JsonIgnore
    public List<NumericFilter> getNumericFilters() {
        return filters.getNumericFilters();
    }

    @JsonIgnore
    public List<TextFilter> getTextFilters() {
        return filters.getTextFilters();
    }

    @JsonIgnore
    public Filters getAllFilters() {
        return filters;
    }
}
