package com.ak.store.common.document.catalogue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDocument {
    private Long id;

    private String title;

    private String description;

    private Integer currentPrice;

    private Integer fullPrice;

    private Integer discountPercentage;

    private Long categoryId;

    private Integer saleAmount;

    private Integer reviewAmount;

    private Float rating;

    private Boolean isAvailable;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductCharacteristicDocument> characteristics;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ImageDocument> images;
}