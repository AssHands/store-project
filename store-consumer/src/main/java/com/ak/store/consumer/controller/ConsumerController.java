package com.ak.store.consumer.controller;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import com.ak.store.consumer.facade.ConsumerServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/consumer/consumers")
public class ConsumerController {
    private final ConsumerServiceFacade consumerServiceFacade;

    @PostMapping
    public String createOne(@RequestBody @Validated(Create.class) ConsumerDTO consumerDTO) {
        return consumerServiceFacade.createOne(consumerDTO);
    }

    @GetMapping("{id}")
    public ConsumerPoorView findOne(@PathVariable String id) {
        return consumerServiceFacade.findOne(id);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable String id) {
        consumerServiceFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public String updateOne(@PathVariable String id, @RequestBody @Validated(Update.class) ConsumerDTO consumerDTO) {
        return consumerServiceFacade.updateOne(id, consumerDTO);
    }

    @GetMapping("exist/{id}")
    public Boolean existOne(@PathVariable String id) {
        return consumerServiceFacade.existOne(id);
    }

    @PostMapping("verify")
    public void verifyOne(@RequestParam String code) {
        consumerServiceFacade.verifyOne(code);
    }

    //-----------

    @GetMapping("me")
    public ConsumerPoorView getMe(@AuthenticationPrincipal Jwt accessToken) {
        return consumerServiceFacade.findOne(accessToken.getSubject());
    }

    @PatchMapping("me")
    public String updateMe(@AuthenticationPrincipal Jwt accessToken,
                           @RequestBody @Validated(Update.class) ConsumerDTO consumerDTO) {
        return consumerServiceFacade.updateOne(accessToken.getSubject(), consumerDTO);
    }

    @DeleteMapping("me")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken) {
        consumerServiceFacade.deleteOne(accessToken.getSubject());
    }
}