package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "category", "textValues", "rangeValues" })
@EqualsAndHashCode(of = { "name", "isText" })
@Builder
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "static-data")
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isText;

    @OneToMany(mappedBy = "characteristic")
    @Builder.Default
    private Set<TextValue> textValues = new HashSet<>();

    @OneToMany(mappedBy = "characteristic")
    @OrderBy("fromValue")
    @Builder.Default
    private Set<RangeValue> rangeValues = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "category_characteristic",
            joinColumns = @JoinColumn(name = "characteristic_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<Category> category = new HashSet<>();

    public Characteristic(Long id) {
        this.id = id;
    }
}