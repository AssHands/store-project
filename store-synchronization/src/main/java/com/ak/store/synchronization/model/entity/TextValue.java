package com.ak.store.synchronization.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = { "characteristic" })
@Entity
public class TextValue {
    @Id
    @GeneratedValue(generator = "tv_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tv_gen",sequenceName = "text_value_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Characteristic characteristic;

    @NotBlank
    private String textValue;
}