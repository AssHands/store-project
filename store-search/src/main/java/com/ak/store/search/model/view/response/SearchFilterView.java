package com.ak.store.search.model.view.response;

import com.ak.store.search.model.view.FiltersView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//todo мб сделать не NON_EMPTY, а NON_NULL? JsonInclude
public class SearchFilterView {
    private Long categoryId;

    @Builder.Default
    private FiltersView filters = new FiltersView();
}
