package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.facade.CartServiceFacade;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/consumer/cart")
public class CartController {
    private final CartServiceFacade cartServiceFacade;

    @GetMapping("{consumerId}")
    public List<CartView> findAll(@PathVariable String consumerId) {
        return cartServiceFacade.findOne(consumerId);
    }

    @PostMapping("{consumerId}/{productId}")
    public void createOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartServiceFacade.createOne(consumerId, productId);
    }

    @DeleteMapping("{consumerId}/{productId}")
    public void deleteOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartServiceFacade.deleteOne(consumerId, productId);
    }

    @GetMapping("me")
    public List<CartView> findAllMe(@AuthenticationPrincipal Jwt accessToken) {
        return cartServiceFacade.findOne(accessToken.getSubject());
    }

    @PostMapping("me/{productId}")
    public void createOneMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartServiceFacade.createOne(accessToken.getSubject(), productId);
    }

    @DeleteMapping("me/{productId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartServiceFacade.deleteOne(accessToken.getSubject(), productId);
    }

    //TODO: CHECK
    //возвращает было ли значение amount максимально возможным
    @PatchMapping("{consumerId}/{productId}")
    public Boolean setProductAmount(@PathVariable String consumerId, @PathVariable Long productId,
                                    @RequestParam @Positive int amount) {
        return cartServiceFacade.setProductAmount(consumerId, productId, amount);
    }

    //TODO: CHECK
    //возвращает было ли значение amount максимально возможным
    @PatchMapping("me/{productId}")
    public Boolean setProductAmount(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId,
                                    @RequestParam @Positive int amount) {
        return cartServiceFacade.setProductAmount(accessToken.getSubject(), productId, amount);
    }
}
