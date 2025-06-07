package com.ak.store.catalogueRatingUpdater.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    private Long id;

    @Min(0)
    private Integer amountReviews;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private Float rating;

    private Boolean isDeleted;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private RatingSummary ratingSummary;
}