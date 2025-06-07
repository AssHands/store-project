package com.ak.store.catalogue.model.entity;

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
public class RatingSummary {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Product product;

    @Builder.Default
    @Min(0)
    private Integer amount = 0;

    @Builder.Default
    @Min(0)
    private Integer sum = 0;

    @Builder.Default
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private Float average = null;
}