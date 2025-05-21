package com.ak.store.search.model.form;

import com.ak.store.search.model.common.SortingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
//todo сделать стандартные фильтры опциональными
public class ProductSearchRequestForm {
    @Size(min = 3, max = 50)
    private String text;

    @Min(1)
    @Max(100)
    private Integer limit = 20;

    @Positive
    private Integer priceFrom;

    @Positive
    private Integer priceTo;

    private Long categoryId;

    private List<Integer> searchAfter = new ArrayList<>();

    @Valid
    private FiltersForm filters = new FiltersForm();

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private SortingType sortingType = SortingType.POPULAR;
}
