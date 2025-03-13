package com.ak.store.common.model.catalogue.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private Integer currentPrice;
    private Integer fullPrice;
    private Integer discountPercentage;
    private Long categoryId;
    private Integer amountReviews;
    private Float grade;
    private Boolean isAvailable;

    @Builder.Default
    private List<ProductCharacteristicDTO> characteristics = new ArrayList<>();

    @Builder.Default
    private List<ImageDTO> images = new ArrayList<>();
}
