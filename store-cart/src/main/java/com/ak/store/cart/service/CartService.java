package com.ak.store.cart.service;

import com.ak.store.cart.mapper.CartMapper;
import com.ak.store.cart.model.dto.CartDTO;
import com.ak.store.cart.repo.CartRedisRepo;
import com.ak.store.cart.validator.service.CartServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRedisRepo cartRedisRepo;
    private final CartMapper cartMapper;
    private final CartServiceValidator cartValidator;

    public CartDTO findOne(UUID userId) {
        return cartMapper.toCartDTO(cartRedisRepo.findOneByUserId(userId));
    }

    public void addOne(UUID userId, Long productId, Integer amount) {
        cartValidator.validateAdding(productId);
        cartRedisRepo.addOne(userId, productId, amount);
    }

    public void removeOne(UUID userId, Long productId) {
        cartRedisRepo.removeOne(userId, productId);
    }
}
