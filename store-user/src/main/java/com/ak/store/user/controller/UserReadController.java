package com.ak.store.user.controller;

import com.ak.store.user.jdbc.UserDao;
import com.ak.store.user.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserReadController {

    private final UserService userService;
    private final UserDao userDao;

    @Autowired
    public UserReadController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

//    @GetMapping
//    public ResponseEntity<UserPageResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int offset,
//                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit,
//                                                   @RequestParam(defaultValue = "createdAt") String sortField,
//                                                   @RequestParam(defaultValue = "DESC") String direction) {
//
//        return new ResponseEntity<>(userService.findAll(offset, limit, sortField, direction),
//                HttpStatus.FOUND);
//    }
}