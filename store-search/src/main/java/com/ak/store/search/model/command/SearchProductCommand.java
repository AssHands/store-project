package com.ak.store.search.model.command;

import com.ak.store.search.model.common.SortingType;
import com.ak.store.search.model.dto.FiltersDTO;
import com.ak.store.search.model.dto.NumericFilterDTO;
import com.ak.store.search.model.dto.TextFilterDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchProductCommand {
    private Integer limit = 20;

    private List<Object> searchAfter = new ArrayList<>();

    private Integer priceFrom;

    private Integer priceTo;

    private String text;

    private Long categoryId;

    private FiltersDTO filters = new FiltersDTO();

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private SortingType sortingType = SortingType.POPULAR;

    public List<NumericFilterDTO> getNumericFilters() {
        return filters.getNumericFilters();
    }

    public List<TextFilterDTO> getTextFilters() {
        return filters.getTextFilters();
    }
}
