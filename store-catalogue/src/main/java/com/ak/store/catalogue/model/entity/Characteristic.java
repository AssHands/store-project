package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "category", "textValues", "rangeValues" })
@EqualsAndHashCode(of = { "name", "isText" })
@Builder
@Entity
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "is_text")
    private boolean isText;

    @OneToMany(mappedBy = "characteristic")
    private Set<TextValue> textValues = new HashSet<>();

    @OneToMany(mappedBy = "characteristic")
    @OrderBy("fromValue")
    private Set<RangeValue> rangeValues = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "characteristic_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> category = new HashSet<>();

    public Characteristic(Long id) {
        this.id = id;
    }
}