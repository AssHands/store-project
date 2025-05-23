package com.ak.store.search.model.dto;

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
public class ProductDTO {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer discountPercentage;

    private Long categoryId;

    private Integer amountSales;

    private Integer amountReviews;

    private Float grade;

    private Boolean isAvailable;

    @Builder.Default
    private List<ProductCharacteristicDTO> characteristics = new ArrayList<>();

    @Builder.Default
    private List<ImageDTO> images = new ArrayList<>();
}
