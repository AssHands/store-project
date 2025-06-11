package com.ak.store.cart.repo;

import com.ak.store.cart.model.document.Cart;
import com.ak.store.cart.model.document.ProductCart;
import com.ak.store.cart.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CartRedisRepoImpl implements CartRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String CART_KEY = "cart:";

    @Override
    public Cart findOneByUserId(UUID userId) {
        var map = stringRedisTemplate.opsForHash().entries(CART_KEY + userId);

        var products = map.entrySet().stream()
                .map(e -> new ProductCart(
                        RedisUtil.parseLong(e.getKey()),
                        RedisUtil.parseInt(e.getValue())
                )).toList();

        return Cart.builder()
                .products(products)
                .build();
    }

    @Override
    public void addOne(UUID userId, Long productId, Integer amount) {
        stringRedisTemplate.opsForHash().put(CART_KEY + userId, productId.toString(), amount.toString());
    }

    @Override
    public void removeOne(UUID userId, Long productId) {
        stringRedisTemplate.opsForHash().delete(CART_KEY + userId, productId.toString());
    }
}