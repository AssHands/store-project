package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

//  @Column(name = "category_id")
//  @ManyToOne
    private Long categoryId;

//    @Min(0)
//    private int amountSales;

    @Min(0)
    private int amountReviews;

    @Min(1)
    @Max(5)
    private float grade;

//    @OneToMany
//    private List<ProductCharacteristic> characteristics;
}