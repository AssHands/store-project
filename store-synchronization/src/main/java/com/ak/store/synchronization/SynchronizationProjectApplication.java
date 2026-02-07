package com.ak.store.synchronization;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class SynchronizationProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronizationProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
