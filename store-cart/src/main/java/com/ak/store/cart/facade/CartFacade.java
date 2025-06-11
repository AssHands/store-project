package com.ak.store.cart.facade;

import com.ak.store.cart.model.dto.CartDTO;
import com.ak.store.cart.service.CartService;
import com.ak.store.cart.mapper.CartMapper;
import com.ak.store.cart.model.view.CartView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CartFacade {
    private final CartService cartService;

    public CartDTO findOne(UUID userId) {
        return cartService.findOne(userId);
    }

    public void addOne(UUID userId, Long productId, Integer amount) {
        cartService.addOne(userId, productId, amount);
    }

    public void removeOne(UUID userId, Long productId) {
        cartService.removeOne(userId, productId);
    }
}