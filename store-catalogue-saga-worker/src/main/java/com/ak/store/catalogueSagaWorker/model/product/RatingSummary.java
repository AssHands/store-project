package com.ak.store.catalogueSagaWorker.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "product")
@Entity
public class RatingSummary {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Product product;

    @Min(0)
    private Integer amount;

    @Min(0)
    private Integer sum;

    private Float average;
}