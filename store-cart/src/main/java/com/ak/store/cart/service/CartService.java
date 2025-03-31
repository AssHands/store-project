package com.ak.store.cart.service;

import com.ak.store.cart.repo.CartRedisRepo;
import com.ak.store.common.model.cart.document.CartDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRedisRepo cartRedisRepo;

    public CartDocument findAll(String consumerId) {
        return cartRedisRepo.findAllByConsumerId(consumerId)
                .orElseThrow(() -> new RuntimeException("cart not found"));
    }

    public void createOne(String consumerId, Long productId) {
        boolean isSuccess = cartRedisRepo.addOneProduct(consumerId, productId);
        if(!isSuccess) {
            throw new RuntimeException("cant create one");
        }
    }

    public void deleteOne(String consumerId, Long productId) {
        boolean isSuccess = cartRedisRepo.deleteOneProduct(consumerId, productId);
        if(!isSuccess) {
            throw new RuntimeException("cant delete one");
        }
    }

    public void setProductAmount(String consumerId, Long productId, int amount) {
        boolean isSuccess = cartRedisRepo.setProductAmount(consumerId, productId, amount);
        if(!isSuccess) {
            throw new RuntimeException("cant set amount");
        }
    }
}
