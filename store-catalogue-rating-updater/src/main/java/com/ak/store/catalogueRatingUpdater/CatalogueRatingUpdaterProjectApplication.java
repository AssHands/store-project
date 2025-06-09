package com.ak.store.catalogueRatingUpdater;

import com.ak.store.catalogueRatingUpdater.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@EnableScheduling
@SpringBootApplication
public class CatalogueRatingUpdaterProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogueRatingUpdaterProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
