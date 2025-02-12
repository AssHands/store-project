package com.ak.store.common.payload.search;

import com.ak.store.common.model.search.dto.FiltersDTO;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.TextFilter;
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
    private FiltersDTO filtersDTO = new FiltersDTO();

    @JsonIgnore
    public List<NumericFilter> getNumericFilters() {
        return filtersDTO.getNumericFilters();
    }

    @JsonIgnore
    public List<TextFilter> getTextFilters() {
        return filtersDTO.getTextFilters();
    }

    @JsonIgnore
    public FiltersDTO getAllFilters() {
        return filtersDTO;
    }
}
