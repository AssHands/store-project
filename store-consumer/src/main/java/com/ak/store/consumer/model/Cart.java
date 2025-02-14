package com.ak.store.consumer.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "consumer" })
@EqualsAndHashCode(exclude = { "consumer" })
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumer consumer;

    private Long productId;

    private Integer amount;
}
