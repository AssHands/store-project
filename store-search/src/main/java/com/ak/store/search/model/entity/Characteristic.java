package com.ak.store.search.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "categories" })
@Entity
public class Characteristic {
    @Id
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean isText;

    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextValue> textValues = new ArrayList<>();

    @Builder.Default
    @OrderBy("fromValue")
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NumericValue> numericValues = new ArrayList<>();

    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "characteristic")
    private List<CategoryCharacteristic> categories = new ArrayList<>();
}