package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString(exclude = { "characteristics" })
@EqualsAndHashCode(of = { "id" })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "static-data")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @NotBlank
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryCharacteristic> characteristics = new ArrayList<>();
}