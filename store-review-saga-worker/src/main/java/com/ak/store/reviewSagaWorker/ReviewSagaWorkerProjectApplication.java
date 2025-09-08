package com.ak.store.reviewSagaWorker;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@EnableScheduling
@SpringBootApplication
public class ReviewSagaWorkerProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewSagaWorkerProjectApplication.class, args);
    }
}