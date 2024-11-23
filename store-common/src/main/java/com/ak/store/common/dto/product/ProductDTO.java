package com.ak.store.common.dto.product;

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

    private Long categoryId;

    private int amountReviews;

    private float grade;
}
