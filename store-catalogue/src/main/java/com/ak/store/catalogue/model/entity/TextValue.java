package com.ak.store.catalogue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "characteristic" })
@EqualsAndHashCode(of = "textValue")
@Entity
@Builder
@Table(name = "text_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "static-data")
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