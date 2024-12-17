package com.ak.store.catalogue.model.entity.ne;

import com.ak.store.catalogue.model.entity.ne.relation.CategoryCharacteristic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString(exclude = { "characteristics" })
@EqualsAndHashCode(of = { "name" })
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @NotBlank
    private String name;

    @ManyToMany
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    private List<CategoryCharacteristic> characteristics;

    public Category(Long id) {
        this.id = id;
    }
}