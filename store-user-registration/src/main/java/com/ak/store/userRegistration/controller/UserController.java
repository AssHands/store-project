package com.ak.store.userRegistration.controller;

import com.ak.store.userRegistration.facade.UserFacade;
import com.ak.store.userRegistration.mapper.UserMapper;
import com.ak.store.userRegistration.model.form.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user-registration")
public class UserController {
    private final UserFacade userFacade;
    private final UserMapper userMapper;

    @PostMapping("register")
    public void registerOne(@RequestBody UserForm request) {
        userFacade.registerOne(userMapper.toUserWriteDTO(request));
    }
}
