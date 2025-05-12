package com.ak.store.common.model.catalogue.viewNew;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductViewNew {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer fullPrice;

    private Integer discountPercentage;

    private Long categoryId;

    private Float grade;

    private Boolean isAvailable;

    private Boolean isDeleted;

    @Builder.Default
    private List<Long> productCharacteristicIds = new ArrayList<>();

    @Builder.Default
    private List<Long> productImageIds = new ArrayList<>();
}
