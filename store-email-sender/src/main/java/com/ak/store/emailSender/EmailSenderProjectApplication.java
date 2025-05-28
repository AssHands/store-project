package com.ak.store.emailSender;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.ak.store.*"})
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class EmailSenderProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailSenderProjectApplication.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
