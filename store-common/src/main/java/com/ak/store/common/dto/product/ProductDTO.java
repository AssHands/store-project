package com.ak.store.common.dto.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    private String title;

    private String description;

    private Float price;

    @JsonPropertyOrder("category_id")
    private Long categoryId;

    private int amountReviews;

    private float grade;
}
