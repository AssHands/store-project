package com.ak.store.catalogue.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private Integer fullPrice;
    private Integer discountPercentage;
    private Long categoryId;
    private Float rating;
    private Integer reviewAmount;
    private Boolean isAvailable;
    private String status;
}