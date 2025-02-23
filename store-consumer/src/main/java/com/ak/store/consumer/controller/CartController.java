package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.facade.CartServiceFacade;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/consumer/consumers/{id}/cart")
public class CartController {
    private final CartServiceFacade cartServiceFacade;

    @GetMapping
    public List<CartView> findAll(@PathVariable Long id) {
        return cartServiceFacade.findOne(id);
    }

    //TODO: CHECK
    //возвращает было ли значение amount максимально возможным
    @PatchMapping("{productId}")
    public Boolean setProductAmount(@PathVariable Long id, @PathVariable Long productId, @RequestParam @Positive int amount) {
        return cartServiceFacade.setProductAmount(id, productId, amount);
    }

    @DeleteMapping("{productId}")
    public void deleteOne(@PathVariable Long id, @PathVariable Long productId) {
        cartServiceFacade.deleteOne(id, productId);
    }

    @PostMapping("{productId}")
    public void createOne(@PathVariable Long id, @PathVariable Long productId) {
        cartServiceFacade.createOne(id, productId);
    }
}
