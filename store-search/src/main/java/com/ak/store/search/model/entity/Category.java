package com.ak.store.search.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "id" })
@Entity
public class Category {
    @Id
    private Long id;

    private Long parentId;

    @NotBlank
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryCharacteristic> characteristics = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "related_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "related_id")
    )
    private Set<Category> relatedCategories = new HashSet<>();
}