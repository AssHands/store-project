package com.ak.store.consumer.service;

import com.ak.store.consumer.feign.WarehouseFeign;
import com.ak.store.consumer.model.entity.Cart;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.repository.CartRepo;
import com.ak.store.consumer.validator.business.CartBusinessValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepo cartRepo;
    private final CartBusinessValidator cartBusinessValidator;
    private final WarehouseFeign warehouseFeign;

    public List<Cart> findAllByConsumerId(Long id) {
        return cartRepo.findAllByConsumerId(id);
    }

    public Cart findOneByConsumerIdAndProductId(Long id, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(id, productId)
                .orElseThrow(() -> new RuntimeException("no cart found"));
    }

    public Boolean setProductAmount(Long id, Long productId, int amount) {
        cartBusinessValidator.validateSetProductAmount(id, productId);
        Cart cart = findOneByConsumerIdAndProductId(id, productId);
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

    public void deleteOne(Long id, Long productId) {
        cartRepo.delete(findOneByConsumerIdAndProductId(id, productId));
    }

    @Transactional
    public void createOne(Long id, Long productId) {
        cartBusinessValidator.validateCreation(id, productId);
        cartRepo.save(Cart.builder()
                .consumer(Consumer.builder().id(id).build())
                .productId(productId)
                .amount(1)
                .build());
    }

    public void deleteAllByProductId(Long productId) {
        cartRepo.deleteAllByProductId(productId);
    }
}
