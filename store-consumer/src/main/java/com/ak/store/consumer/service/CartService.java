package com.ak.store.consumer.service;

import com.ak.store.consumer.feign.WarehouseFeign;
import com.ak.store.consumer.model.entity.Cart;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.repository.CartRepo;
import com.ak.store.consumer.validator.business.CartBusinessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepo cartRepo;
    private final CartBusinessValidator cartBusinessValidator;
    private final WarehouseFeign warehouseFeign;

    public List<Cart> findAllByConsumerId(String consumerId) {
        return cartRepo.findAllByConsumerId(consumerId);
    }

    public Cart findOneByConsumerIdAndProductId(String consumerId, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(consumerId, productId)
                .orElseThrow(() -> new RuntimeException("no cart found"));
    }

    public Boolean setProductAmount(String consumerId, Long productId, int amount) {
        cartBusinessValidator.validateSetProductAmount(consumerId, productId);
        Cart cart = findOneByConsumerIdAndProductId(consumerId, productId);
        int maxAmount = warehouseFeign.getAmount(productId);
        boolean isMax = false;

        if(amount > maxAmount) {
            cart.setAmount(maxAmount);
            isMax = true;
        } else {
            cart.setAmount(amount);
        }

        cartRepo.save(cart);
        return isMax;
    }

    public void deleteOne(String consumerId, Long productId) {
        cartRepo.delete(findOneByConsumerIdAndProductId(consumerId, productId));
    }

    @Transactional
    public void createOne(String consumerId, Long productId) {
        cartBusinessValidator.validateCreation(consumerId, productId);
        cartRepo.save(Cart.builder()
                //TODO
                //.consumer(Consumer.builder().id(id).build())
                .productId(productId)
                .amount(1)
                .build());
    }

    public void deleteAllByProductId(Long productId) {
        cartRepo.deleteAllByProductId(productId);
    }
}
