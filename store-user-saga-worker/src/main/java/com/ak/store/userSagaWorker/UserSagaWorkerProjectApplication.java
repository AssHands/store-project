package com.ak.store.userSagaWorker;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class UserSagaWorkerProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserSagaWorkerProjectApplication.class, args);
    }
}