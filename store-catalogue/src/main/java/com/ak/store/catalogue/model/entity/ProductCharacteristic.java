package com.ak.store.catalogue.model.entity;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "product" })
@EqualsAndHashCode(of = { "product" })
@Entity
@Table(name = "product_characteristic")
public class ProductCharacteristic {
    @Id
    @GeneratedValue(generator = "pc_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "pc_gen",sequenceName = "product_characteristic_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characteristic_id")
    private Characteristic characteristic;

    //todo: сделать кастомные аннотации,
    // которые проверяют если numeric value == null, то text value не должно быть null. и наоборот
    private Integer numericValue;

    private String textValue;
}