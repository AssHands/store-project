package com.ak.store.catalogue.model.entity;

import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"category"})
@Entity
public class Product {
    @Id
    @GeneratedValue(generator = "p_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "p_gen",sequenceName = "product_id_seq", allocationSize = 20)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    @Size(min = 5, max = 150)
    private String description;

    @Min(1)
    @Max(10_000_000)
    private float price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Min(0)
    @Column(name = "amount_reviews")
    private int amountReviews;

    @Min(1)
    @Max(5)
    private float grade;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductCharacteristic> characteristics = new ArrayList<>();

    public void addCharacteristics(List<ProductCharacteristic> characteristics) {
        this.characteristics.addAll(characteristics);
    }

    public void addCharacteristic(ProductCharacteristic characteristic) {
        this.characteristics.add(characteristic);
    }
}