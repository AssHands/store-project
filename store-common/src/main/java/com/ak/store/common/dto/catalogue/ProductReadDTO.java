package com.ak.store.common.dto.catalogue;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    @JsonPropertyOrder("category_id")
    private Long categoryId;

    private int amountReviews;

    private float grade;
}
