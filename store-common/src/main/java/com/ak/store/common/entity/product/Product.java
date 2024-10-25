package com.ak.store.common.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    @Size(min = 5, max = 150)
    private String description;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private Category categoryId;
}