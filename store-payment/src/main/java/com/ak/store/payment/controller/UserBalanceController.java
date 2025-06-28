package com.ak.store.payment.controller;

import com.ak.store.payment.facade.UserBalanceFacade;
import com.ak.store.payment.mapper.UserBalanceMapper;
import com.ak.store.payment.model.view.UserBalanceView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/payment/user-balance")
public class UserBalanceController {
    private final UserBalanceFacade userBalanceFacade;
    private final UserBalanceMapper userBalanceMapper;

    @GetMapping
    public UserBalanceView findOne(@AuthenticationPrincipal Jwt accessToken) {
        UUID id = UUID.fromString(accessToken.getSubject());
        return userBalanceMapper.toUserBalanceView(userBalanceFacade.findOne(id));
    }
}