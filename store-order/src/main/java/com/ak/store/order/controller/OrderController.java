package com.ak.store.order.controller;

import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping
    public List<OrderView> findAllOrder(@AuthenticationPrincipal Jwt accessToken) {
        return orderFacade.findAllByConsumerId(accessToken.getSubject());
    }

    @PostMapping
    public void createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody OrderDTO orderDTO) {
        orderFacade.createOne(accessToken, orderDTO);
    }
}
