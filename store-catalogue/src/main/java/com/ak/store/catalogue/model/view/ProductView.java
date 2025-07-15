package com.ak.store.catalogue.model.view;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductView {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer fullPrice;

    private Integer discountPercentage;

    private Long categoryId;

    private Float rating;

    private Integer reviewAmount;

    private Boolean isAvailable;

    private String status;
}
