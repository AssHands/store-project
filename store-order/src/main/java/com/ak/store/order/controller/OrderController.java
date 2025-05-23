package com.ak.store.order.controller;

import com.ak.store.order.facade.OrderFacade;
import com.ak.store.order.model.dto.UserAuthContext;
import com.ak.store.order.model.form.OrderForm;
import com.ak.store.order.model.view.OrderViewPayload;
import com.ak.store.order.mapper.OrderMapper;
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
    //todo сделать пагинацию
    public List<OrderViewPayload> findAll(@RequestParam UUID userId) {
        return orderMapper.toOrderViewPayload(orderFacade.findAllByUserId(userId));
    }

    @GetMapping("me")
    public List<OrderViewPayload> findMyAll(@AuthenticationPrincipal Jwt accessToken) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        return orderMapper.toOrderViewPayload(orderFacade.findAllByUserId(userId));
    }

    @PostMapping
    public void createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody OrderForm request) {
        var authContext = new UserAuthContext(
                UUID.fromString(accessToken.getSubject()),
                accessToken.getClaimAsString("email")
        );

        orderFacade.createOne(authContext, orderMapper.toOrderWriteDTO(request));
    }
}
