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
        return cartFacade.findAll(consumerId);
    }

    @PostMapping("{consumerId}/{productId}")
    public void createOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartFacade.createOne(consumerId, productId);
    }

    @DeleteMapping("{consumerId}/{productId}")
    public void deleteOne(@PathVariable String consumerId, @PathVariable Long productId) {
        cartFacade.deleteOne(consumerId, productId);
    }

    //TODO: CHECK
    @PatchMapping("{consumerId}/{productId}")
    public CartView setProductAmount(@PathVariable String consumerId, @PathVariable Long productId,
                                    @RequestParam @Positive int amount) {
        return cartFacade.setProductAmount(consumerId, productId, amount);
    }

    //-------------------------

    @GetMapping("me")
    public List<CartView> findAllMe(@AuthenticationPrincipal Jwt accessToken) {
        return cartFacade.findAll(accessToken.getSubject());
    }

    @PostMapping("me/{productId}")
    public void createOneMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartFacade.createOne(accessToken.getSubject(), productId);
    }

    @DeleteMapping("me/{productId}")
    public void deleteOneMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId) {
        cartFacade.deleteOne(accessToken.getSubject(), productId);
    }

    //TODO: CHECK
    @PatchMapping("me/{productId}")
    public CartView setProductAmountMe(@AuthenticationPrincipal Jwt accessToken, @PathVariable Long productId,
                                      @RequestParam @Positive int amount) {
        return cartFacade.setProductAmount(accessToken.getSubject(), productId, amount);
    }
}
