package com.ak.store.search.model.form.request;

import com.ak.store.search.model.form.FiltersForm;
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
//todo add validation
public class FilterSearchRequestForm {
    private Long categoryId;

    private Integer priceFrom;

    private Integer priceTo;

    private String text;

    private FiltersForm filters = new FiltersForm();
}
