package com.ak.store.payment.controller;

import com.ak.store.payment.facade.UserBalanceFacade;
import com.ak.store.payment.mapper.UserBalanceMapper;
import com.ak.store.payment.model.view.UserBalanceView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/payment/user-balance")
public class UserBalanceController {
    private final UserBalanceFacade userBalanceFacade;
    private final UserBalanceMapper userBalanceMapper;

    @GetMapping
    public UserBalanceView findOne(@RequestParam UUID userId) {
        return userBalanceMapper.toUserBalanceView(userBalanceFacade.findOne(userId));
    }

    @GetMapping("me")
    public UserBalanceView findOneMe(@AuthenticationPrincipal Jwt accessToken) {
        UUID id = UUID.fromString(accessToken.getSubject());
        return userBalanceMapper.toUserBalanceView(userBalanceFacade.findOne(id));
    }
}