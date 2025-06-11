package com.ak.store.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CartProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartProjectApplication.class, args);
    }
}