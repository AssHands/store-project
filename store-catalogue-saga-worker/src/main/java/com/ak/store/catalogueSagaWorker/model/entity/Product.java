package com.ak.store.catalogueSagaWorker.model.entity;

import jakarta.persistence.*;
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
    private Integer reviewAmount;

    private Float rating;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private RatingSummary ratingSummary;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;
}