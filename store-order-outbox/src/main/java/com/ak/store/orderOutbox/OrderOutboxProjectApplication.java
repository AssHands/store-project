package com.ak.store.orderOutbox;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.ak.store.*" })
@EnableScheduling
@SpringBootApplication
public class OrderOutboxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderOutboxProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}