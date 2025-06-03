package com.ak.store.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
//todo добавить удаление всех отзывов при удалении продукта (кафка)
//todo добавить функционал реакций
public class ReviewProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewProjectApplication.class, args);
    }
}