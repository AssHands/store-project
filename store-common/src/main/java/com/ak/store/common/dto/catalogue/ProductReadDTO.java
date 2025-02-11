package com.ak.store.common.dto.catalogue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
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
public class ProductReadDTO {
    private Long id;

    private String title;

    private String description;

    private int currentPrice;

    private int fullPrice;

    private int discountPercentage;

    private int amountReviews;

    private float grade;

    @Valid
    private CategoryDTO category;

    private List<ImageDTO> images;

    @Builder.Default
    private List<@Valid ProductCharacteristicDTO> characteristics = new ArrayList<>();
}
