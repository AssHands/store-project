package com.ak.store.catalogueSagaWorker;

import com.ak.store.catalogueSagaWorker.util.LocalDateTimeAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class CatalogueSagaWorkerProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogueSagaWorkerProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}