package com.ak.store.recommendation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "product")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDocument {
    private Long id;

    private String title;

    private String description;

    private int currentPrice;

    private int discountPercentage;

    private Long categoryId;

    private int amountSales;

    private int amountReviews;

    private float grade;

    private Boolean isAvailable;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductCharacteristicDocument> characteristics;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ImageDocument> images;
}