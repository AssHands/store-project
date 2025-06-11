package com.ak.store.cart.controller;

import com.ak.store.cart.facade.CartFacade;
import com.ak.store.cart.mapper.CartMapper;
import com.ak.store.cart.model.view.CartView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartFacade cartFacade;
    private final CartMapper cartMapper;

    @GetMapping
    public CartView findOne(@AuthenticationPrincipal Jwt accessToken) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return cartMapper.toCartView(cartFacade.findOne(userId));
    }

    @PostMapping("{productId}")
    public void addOne(@AuthenticationPrincipal Jwt accessToken,
                       @PathVariable Long productId, @RequestParam Integer amount) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        cartFacade.addOne(userId, productId, amount);
    }

    @DeleteMapping("{productId}")
    public void removeOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        cartFacade.removeOne(userId, productId);
    }
}