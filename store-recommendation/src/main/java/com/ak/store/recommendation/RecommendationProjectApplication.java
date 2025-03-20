package com.ak.store.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ak.store.*"})
@SpringBootApplication
public class RecommendationProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendationProjectApplication.class, args);
    }
}