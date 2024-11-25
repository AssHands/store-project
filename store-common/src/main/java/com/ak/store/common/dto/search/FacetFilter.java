package com.ak.store.common.dto.search;

import com.ak.store.common.dto.search.nested.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "categoryId", "filters" })
public class FacetFilter {
    @JsonProperty("category_id")
    private int categoryId;

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