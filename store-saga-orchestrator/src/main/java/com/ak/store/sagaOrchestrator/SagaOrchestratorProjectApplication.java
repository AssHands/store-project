package com.ak.store.sagaOrchestrator;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SagaOrchestratorProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SagaOrchestratorProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}