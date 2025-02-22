package com.ak.store.order.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"order"})
@Builder
@Entity
@Table(name = "orders_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private Long productId;

    private Integer amount;

    private Integer pricePerOne;
}
