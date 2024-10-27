package com.ak.store.user.controller;

import com.ak.store.common.ResponseObject.UserPageResponse;
import com.ak.store.common.entity.product.Product;
import com.ak.store.common.entity.user.User;
import com.ak.store.filterQuery.FilterQueryGenerator;
import com.ak.store.user.jdbc.UserDao;
import com.ak.store.user.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

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

    @GetMapping
    public ResponseEntity<UserPageResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int offset,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit,
                                                   @RequestParam(defaultValue = "createdAt") String sortField,
                                                   @RequestParam(defaultValue = "DESC") String direction) {

        return new ResponseEntity<>(userService.findAll(offset, limit, sortField, direction),
                HttpStatus.FOUND);
    }

    @GetMapping("find")
    public ResponseEntity<UserPageResponse> getAllByName(@RequestParam String name) {
        return new ResponseEntity<>(userService.findAllByName(name), HttpStatus.FOUND);
    }

    @GetMapping("p")
    public String p() {
        return FilterQueryGenerator.generateQuery("{\"name\": \"John Doe\", \"age\": 30, \"city\": \"New York\"}");
    }
    @GetMapping("pp")
    public String p(@RequestBody HashMap<String, String> filters) {
        return FilterQueryGenerator.generateQueryMap(filters);
    }
}