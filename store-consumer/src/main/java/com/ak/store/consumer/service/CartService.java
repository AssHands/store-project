package com.ak.store.consumer.service;

import com.ak.store.consumer.model.Cart;
import com.ak.store.consumer.model.Consumer;
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

    public List<Cart> findAllByConsumerId(Long id) {
        return cartRepo.findAllByConsumerId(id);
    }

    public Cart findOneByConsumerIdAndProductId(Long id, Long productId) {
        return cartRepo.findByConsumerIdAndProductId(id, productId)
                .orElseThrow(() -> new RuntimeException("no cart found"));
    }


    //todo: проверять существует ли такое кол-во продуктов у миркосервиса склада
    public void setProductAmount(Long id, Long productId, int amount) {
        cartBusinessValidator.validateSetAmountProducts(id, productId);
        Cart cart = findOneByConsumerIdAndProductId(id, productId);
        cart.setAmount(amount);
        cartRepo.save(cart);
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
