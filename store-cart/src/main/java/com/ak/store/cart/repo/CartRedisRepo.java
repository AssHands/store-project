package com.ak.store.cart.repo;

import com.ak.store.cart.model.document.Cart;

import java.util.UUID;

public interface CartRedisRepo {
     Cart findOneByUserId(UUID userId);
     void addOne(UUID userId, Long productId, Integer amount);
     void removeOne(UUID userId, Long productId);
}