package com.ak.store.common.dto.catalogue.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {
    private Long id;

    private String title;

    private String description;

    private Float price;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("amount_reviews")
    private int amountReviews;

    private float grade;
}
