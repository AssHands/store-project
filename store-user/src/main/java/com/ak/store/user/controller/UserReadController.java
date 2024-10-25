package com.ak.store.user.controller;

import com.ak.store.common.entity.user.User;
import com.ak.store.user.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserReadController {

    private final UserService userService;

    @Autowired
    public UserReadController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Slice<User>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int offset,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit,
                                                   @RequestParam(defaultValue = "popular") String sort,
                                                   @RequestParam(required = false) List<Integer> filters) {

        return new ResponseEntity(userService.findAll(offset, limit), HttpStatus.FOUND);
    }
}