package com.ak.store.common.payload.search;

import com.ak.store.common.model.search.dto.FiltersDTO;
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
public class SearchAvailableFiltersResponse {

    private Long categoryId;
    @Builder.Default
    private FiltersView filtersView = new FiltersView();

    @JsonIgnore
    public List<NumericFilter> getNumericFilters() {
        return filtersView.getNumericFilters();
    }

    @JsonIgnore
    public List<TextFilter> getTextFilters() {
        return filtersView.getTextFilters();
    }

    @JsonIgnore
    public FiltersView getAllFilters() {
        return filtersView;
    }
}
