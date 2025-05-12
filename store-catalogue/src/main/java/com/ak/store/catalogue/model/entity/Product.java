package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(generator = "p_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "p_gen",sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Size(min = 5, max = 70)
    private String title;

    @NotBlank
    @Size(min = 5, max = 150)
    private String description;

    @Min(1)
    @Max(10_000_000)
    private int currentPrice;

    @Min(1)
    @Max(10_000_000)
    private int fullPrice;

    @Min(0)
    @Max(99)
    private int discountPercentage;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Min(0)
    @Column(name = "amount_reviews")
    private int amountReviews;

    //todo: check it work
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "5.0")
    private Float grade;

    private Boolean isAvailable;

    private Boolean isDeleted;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCharacteristic> characteristics = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addCharacteristics(List<ProductCharacteristic> characteristics) {
        this.characteristics.addAll(characteristics);
    }

    public void addCharacteristic(ProductCharacteristic characteristic) {
        this.characteristics.add(characteristic);
    }
}