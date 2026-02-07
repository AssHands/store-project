package com.ak.store.search.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = { "characteristic" })
@Entity
public class NumericValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Characteristic characteristic;

    @NotNull
    private Integer fromValue;

    @NotNull
    private Integer toValue;
}