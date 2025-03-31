package com.ak.store.cart;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CartProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}