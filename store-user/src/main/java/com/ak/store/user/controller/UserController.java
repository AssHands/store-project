package com.ak.store.user.controller;

import com.ak.store.common.model.consumer.form.UserForm;
import com.ak.store.user.facade.UserFacade;
import com.ak.store.user.model.validationGroup.Create;
import com.ak.store.user.model.validationGroup.Update;
import com.ak.store.user.model.view.UserView;
import com.ak.store.user.mapper.UserMapper;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/users")
public class UserController {
    private final UserFacade userFacade;
    private final UserMapper userMapper;

    @GetMapping("{id}")
    public UserView findOne(@PathVariable UUID id) {
        return userMapper.toUserView(userFacade.findOne(id));
    }

    @PostMapping
    public UUID createOne(@RequestBody @Validated(Create.class) UserForm request) {
        var user = userFacade.createOne(userMapper.toUserWriteDTO(request));
        return user.getId();
    }

    @PatchMapping("{id}")
    public UUID updateOne(@PathVariable UUID id, @RequestBody @Validated(Update.class) UserForm request) {
        return userFacade.updateOne(id, userMapper.toUserWriteDTO(request)).getId();
    }

    @DeleteMapping("{id}")
    public void deleteMe(@PathVariable UUID id) {
        userFacade.deleteOne(id);
    }

    @PostMapping("verify")
    public UUID verifyOne(@RequestParam String code) {
        return userFacade.verifyOne(code).getId();
    }

    @GetMapping("exist/{id}")
    public Boolean isExistOne(@PathVariable UUID id) {
        return userFacade.isExistOne(id);
    }

    //--- AUTH ---

    @GetMapping("me")
    public UserView findMe(@AuthenticationPrincipal Jwt accessToken) {
        var user = userFacade.findOne(UUID.fromString(accessToken.getSubject()));
        return userMapper.toUserView(user);
    }

    @PatchMapping("me")
    public UUID updateMe(@AuthenticationPrincipal Jwt accessToken,
                           @RequestBody @Validated(Update.class) UserForm request) {
        var user = userFacade.updateOne(UUID.fromString(accessToken.getSubject()), userMapper.toUserWriteDTO(request));
        return user.getId();
    }

    @DeleteMapping("me")
    public void deleteMe(@AuthenticationPrincipal Jwt accessToken) {
        userFacade.deleteOne(UUID.fromString(accessToken.getSubject()));
    }

    @PatchMapping("me/email")
    public UUID updateOneEmail(@AuthenticationPrincipal Jwt accessToken, @RequestBody @Email String email) {
        var user = userFacade.updateOneEmail(UUID.fromString(accessToken.getSubject()), email);
        return user.getId();
    }
}