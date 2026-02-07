package com.ak.store.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = RedisAutoConfiguration.class, scanBasePackages = "com.ak.store.*")
public class RecommendationProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendationProjectApplication.class, args);
    }
}