package com.ak.store.consumer.controller;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import com.ak.store.consumer.facade.ConsumerFacade;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/consumer/consumers")
public class ConsumerController {
    private final ConsumerFacade consumerFacade;

    @PostMapping
    public String createOne(@RequestBody @Validated(Create.class) ConsumerDTO consumerDTO) {
        return consumerFacade.createOne(consumerDTO);
    }

    @GetMapping("{id}")
    public ConsumerPoorView findOne(@PathVariable String id) {
        return consumerFacade.findOne(id);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable String id) {
        consumerFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public String updateOne(@PathVariable String id, @RequestBody @Validated(Update.class) ConsumerDTO consumerDTO) {
        return consumerFacade.updateOne(id, consumerDTO);
    }

    @GetMapping("exist/{id}")
    public Boolean existOne(@PathVariable String id) {
        return consumerFacade.existOne(id);
    }

    @PostMapping("verify")
    public String verifyOne(@RequestParam String code) {
        return consumerFacade.verifyOne(code);
    }

    //-----------------------

    @GetMapping("me")
    public ConsumerPoorView getMe(@AuthenticationPrincipal Jwt accessToken) {
        return consumerFacade.findOne(accessToken.getSubject());
    }

    @PatchMapping("me")
    public String updateMe(@AuthenticationPrincipal Jwt accessToken,
                           @RequestBody @Validated(Update.class) ConsumerDTO consumerDTO) {
        return consumerFacade.updateOne(accessToken.getSubject(), consumerDTO);
    }

    @DeleteMapping("me")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken) {
        consumerFacade.deleteOne(accessToken.getSubject());
    }

    @PatchMapping("email")
    public String updateOneEmail(@AuthenticationPrincipal Jwt accessToken, @RequestBody @Email String email) {
        return consumerFacade.updateOneEmail(accessToken.getSubject(), email);
    }
}