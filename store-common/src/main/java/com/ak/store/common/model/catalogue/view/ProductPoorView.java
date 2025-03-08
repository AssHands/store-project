package com.ak.store.common.model.catalogue.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductPoorView {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer fullPrice;

    private Integer discountPercentage;

    private Integer amountReviews;

    private Float grade;

    private List<ImageView> images = new ArrayList<>();
}