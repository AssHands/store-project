package com.ak.store.cart.repo;

import com.ak.store.common.model.cart.document.CartDocument;
import com.ak.store.common.model.cart.document.ProductCartDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CartRedisRepoImpl implements CartRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;

    private static final String CART_KEY = "cart:";

    @Override
    public Optional<CartDocument> findAllByConsumerId(String consumerId) {
        var list = stringRedisTemplate.opsForList().range(CART_KEY + consumerId, 0, -1);

        if (list == null) {
            return Optional.empty();
        }

        return Optional.of(CartDocument.builder().products(list.stream()
                        .map(v -> gson.fromJson(v, ProductCartDocument.class))
                        .toList())
                .build());
    }

    @Override
    public boolean addOneProduct(String consumerId, Long productId) {
        var list = stringRedisTemplate.opsForList().range(CART_KEY + consumerId, 0, -1);
        boolean isExist = false;

        if (list != null) {
            isExist = list.stream()
                    .map(v -> gson.fromJson(v, ProductCartDocument.class))
                    .anyMatch(v -> v.getProductId().equals(productId));
        }

        if (!isExist) {
            ProductCartDocument productCartDocument = new ProductCartDocument();
            productCartDocument.setProductId(productId);
            productCartDocument.setAmount(1);

            stringRedisTemplate.opsForList().rightPush(CART_KEY + consumerId, gson.toJson(productCartDocument));
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteOneProduct(String consumerId, Long productId) {
        var cart = stringRedisTemplate.opsForList().range(CART_KEY + consumerId, 0, -1);
        Optional<ProductCartDocument> product = Optional.empty();

        if (cart != null) {
            product = cart.stream()
                    .map(v -> gson.fromJson(v, ProductCartDocument.class))
                    .filter(v -> v.getProductId().equals(productId))
                    .findFirst();
        }

        if (product.isEmpty()) {
            return false;
        }

        stringRedisTemplate.opsForList().remove(CART_KEY + consumerId, 1, gson.toJson(product.get()));
        return true;
    }

    @Override
    public boolean setProductAmount(String consumerId, Long productId, int amount) {
        var cart = stringRedisTemplate.opsForList().range(CART_KEY + consumerId, 0, -1);
        Optional<ProductCartDocument> product = Optional.empty();

        if (cart != null) {
            product = cart.stream()
                    .map(v -> gson.fromJson(v, ProductCartDocument.class))
                    .filter(v -> v.getProductId().equals(productId))
                    .findFirst();
        }

        if (product.isEmpty()) {
            return false;
        }

        stringRedisTemplate.opsForList().remove(CART_KEY + consumerId, 1, gson.toJson(product.get()));
        stringRedisTemplate.opsForList().rightPush(CART_KEY + consumerId, gson.toJson(product.get()));
        return true;
    }
}
