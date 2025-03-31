package com.ak.store.cart.facade;

import com.ak.store.cart.service.CartService;
import com.ak.store.cart.util.mapper.CartMapper;
import com.ak.store.common.model.cart.view.CartView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartFacade {
    private final CartService cartService;
    private final CartMapper cartMapper;

    public CartView findAll(String consumerId) {
        return cartMapper.toCartView(cartService.findAll(consumerId));
    }

    public void createOne(String consumerId, Long productId) {
        cartService.createOne(consumerId, productId);
    }

    public void deleteOne(String consumerId, Long productId) {
        cartService.deleteOne(consumerId, productId);
    }

    public void setProductAmount(String consumerId, Long productId, int amount) {
        cartService.setProductAmount(consumerId, productId, amount);
    }
}
