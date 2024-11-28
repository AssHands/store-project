package com.ak.store.common.payload.search;

import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableFiltersResponse {
    Filters filters = new Filters();

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
