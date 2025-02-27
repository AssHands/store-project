package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.service.CartService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CartServiceFacade {
    private final ConsumerMapper consumerMapper;
    private final CartService cartService;

    public List<CartView> findOne(String consumerId) {
        return cartService.findAllByConsumerId(consumerId).stream()
                .map(consumerMapper::mapToCartView)
                .toList();
    }

    @Transactional
    public Boolean setProductAmount(String consumerId, Long productId, int amount) {
        return cartService.setProductAmount(consumerId, productId, amount);
    }

    @Transactional
    public void deleteOne(String consumerId, Long productId) {
        cartService.deleteOne(consumerId, productId);
    }

    @Transactional
    public void createOne(String consumerId, Long productId) {
        cartService.createOne(consumerId, productId);
    }

    @Transactional
    public void deleteAllByProductId(Long productId) {
        cartService.deleteAllByProductId(productId);
    }
}
