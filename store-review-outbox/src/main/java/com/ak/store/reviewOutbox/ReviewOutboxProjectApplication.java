package com.ak.store.reviewOutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReviewOutboxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewOutboxProjectApplication.class, args);
    }
}
