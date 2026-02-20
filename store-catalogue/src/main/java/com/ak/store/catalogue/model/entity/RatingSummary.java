package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
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
    private Float average = 0f;
}