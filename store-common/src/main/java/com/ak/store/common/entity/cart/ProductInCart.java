package com.ak.store.common.entity.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCart {
    private Long productId;
    private int amount;
}
