package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(of = { "imageKey" })
@ToString(of = { "imageKey", "index" })
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(generator = "pi_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "pi_gen",sequenceName = "product_image_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank
    @Column(name = "image_key")
    private String imageKey;

    @NotNull
    @Min(0)
    @Max(9)
    private Integer index;
}
