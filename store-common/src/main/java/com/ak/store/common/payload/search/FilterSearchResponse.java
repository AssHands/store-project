package com.ak.store.common.payload.search;

import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.TextFilter;
import com.ak.store.common.model.search.view.FiltersView;
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
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterSearchResponse {

    private Long categoryId;
    @Builder.Default
    private FiltersView filters = new FiltersView();

    @JsonIgnore
    public List<NumericFilter> getNumericFilters() {
        return filters.getNumericFilters();
    }

    @JsonIgnore
    public List<TextFilter> getTextFilters() {
        return filters.getTextFilters();
    }

    @JsonIgnore
    public FiltersView getAllFilters() {
        return filters;
    }
}
