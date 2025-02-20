package com.ak.store.consumer.facade;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.feign.CatalogueFeign;
import com.ak.store.consumer.model.Cart;
import com.ak.store.consumer.service.CartService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartServiceFacade {
    private final ConsumerMapper consumerMapper;
    private final CartService cartService;
    private final CatalogueFeign catalogueFeign;

    public List<CartView> findOne(Long id) {
        List<Cart> cartList = cartService.findAllByConsumerId(id);

        Map<Long, Optional<ProductPoorView>> map = cartList.stream()
                .collect(Collectors.toMap(Cart::getProductId, c -> Optional.empty()));

        var productList = catalogueFeign.findAllProductPoor(map.keySet().stream().toList());
        for(var product : productList) {
            map.replace(product.getId(), Optional.of(product));
        }

        List<CartView> cartViewList = new ArrayList<>();
        for(var cart : cartList) {
            CartView cartView = consumerMapper.mapToCartView(cart);
            cartView.setProduct(map.get(cart.getProductId()).orElse(null));
            if(cartView.getProduct() == null) continue;

            cartViewList.add(cartView);
        }

        return cartViewList;
    }

    @Transactional
    public void setProductAmount(Long id, Long productId, int amount) {
        cartService.setProductAmount(id, productId, amount);
    }

    @Transactional
    public void deleteOne(Long id, Long productId) {
        cartService.deleteOne(id, productId);
    }

    @Transactional
    public void createOne(Long id, Long productId) {
        cartService.createOne(id, productId);
    }

    @Transactional
    public void deleteAllByProductId(Long productId) {
        cartService.deleteAllByProductId(productId);
    }
}
