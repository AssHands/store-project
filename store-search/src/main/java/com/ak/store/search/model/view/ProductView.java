package com.ak.store.search.model.view;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductView {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer discountPercentage;

    private Long categoryId;

    private Integer saleAmount;

    private Integer reviewAmount;

    private Float rating;

    private Boolean isAvailable;

    @Builder.Default
    private List<ProductCharacteristicView> characteristics = new ArrayList<>();

    @Builder.Default
    private List<ImageView> images = new ArrayList<>();
}