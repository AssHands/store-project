package com.ak.store.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ak.store.*"})
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
public class RecommendationProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendationProjectApplication.class, args);
    }
}