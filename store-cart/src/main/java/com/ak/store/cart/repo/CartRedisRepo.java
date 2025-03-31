package com.ak.store.cart.repo;

import com.ak.store.common.model.cart.document.CartDocument;

import java.util.Optional;

public interface CartRedisRepo {
     Optional<CartDocument> findAllByConsumerId(String consumerId);
     boolean addOneProduct(String consumerId, Long productId);
     boolean deleteOneProduct(String consumerId, Long productId);
     boolean setProductAmount(String consumerId, Long productId, int amount);
}
