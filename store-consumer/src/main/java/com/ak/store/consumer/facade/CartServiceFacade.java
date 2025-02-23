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

    public List<CartView> findOne(Long id) {
        return cartService.findAllByConsumerId(id).stream()
                .map(consumerMapper::mapToCartView)
                .toList();
    }

    @Transactional
    public Boolean setProductAmount(Long id, Long productId, int amount) {
        return cartService.setProductAmount(id, productId, amount);
    }

    @Transactional
    public void deleteOne(Long id, Long productId) {
        cartService.deleteOne(id, productId);
    }

    @Transactional
    public void createOne(Long id, Long productId) {
        cartService.createOne(id, productId);
    }

    @Transactional
    public void deleteAllByProductId(Long productId) {
        cartService.deleteAllByProductId(productId);
    }
}
