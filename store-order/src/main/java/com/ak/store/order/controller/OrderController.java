package com.ak.store.order.controller;

import com.ak.store.order.facade.OrderFacade;
import com.ak.store.order.mapper.OrderMapper;
import com.ak.store.order.model.form.OrderForm;
import com.ak.store.order.model.view.OrderPayloadView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order/orders")
public class OrderController {
    private final OrderFacade orderFacade;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderPayloadView> findAll(@RequestParam UUID userId) {
        return orderFacade.findAllByUserId(userId).stream()
                .map(orderMapper::toPayloadView)
                .toList();
    }

    @GetMapping("me")
    public List<OrderPayloadView> findMyAll(@AuthenticationPrincipal Jwt accessToken) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return orderFacade.findAllByUserId(userId).stream()
                .map(orderMapper::toPayloadView)
                .toList();
    }

    @PostMapping
    public void createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody OrderForm form) {
        var command = orderMapper.toWriteCommand(
                form,
                UUID.fromString(accessToken.getSubject()),
                accessToken.getClaimAsString("email")
        );

        orderFacade.createOne(command);
    }
}
