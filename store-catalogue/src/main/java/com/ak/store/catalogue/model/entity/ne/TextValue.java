package com.ak.store.catalogue.model.entity.ne;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "characteristic" })
@EqualsAndHashCode(of = "textValue")
@Entity
@Builder
@Table(name = "text_value")
public class TextValue {
    @Id
    @GeneratedValue(generator = "tv_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tv_gen",sequenceName = "text_value_id_seq", allocationSize = 20)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Characteristic characteristic;

    private String textValue;
}