package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.facade.CartFacade;
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
    private final CartFacade cartFacade;

    @GetMapping("{consumerId}")
    public List<CartView> findAll(@PathVariable String consumerId) {
        return cartFacade.findOne(consumerId);
    }

    @PostMapping("{consumerId}/{productId}")
    public void createOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartFacade.createOne(consumerId, productId);
    }

    @DeleteMapping("{consumerId}/{productId}")
    public void deleteOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartFacade.deleteOne(consumerId, productId);
    }

    @GetMapping("me")
    public List<CartView> findAllMe(@AuthenticationPrincipal Jwt accessToken) {
        return cartFacade.findOne(accessToken.getSubject());
    }

    @PostMapping("me/{productId}")
    public void createOneMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartFacade.createOne(accessToken.getSubject(), productId);
    }

    @DeleteMapping("me/{productId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartFacade.deleteOne(accessToken.getSubject(), productId);
    }

    //TODO: CHECK
    //возвращает было ли значение amount максимально возможным
    @PatchMapping("{consumerId}/{productId}")
    public Boolean setProductAmount(@PathVariable String consumerId, @PathVariable Long productId,
                                    @RequestParam @Positive int amount) {
        return cartFacade.setProductAmount(consumerId, productId, amount);
    }

    //TODO: CHECK
    //возвращает было ли значение amount максимально возможным
    @PatchMapping("me/{productId}")
    public Boolean setProductAmount(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId,
                                    @RequestParam @Positive int amount) {
        return cartFacade.setProductAmount(accessToken.getSubject(), productId, amount);
    }
}
