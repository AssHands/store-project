package com.ak.store.emailSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ak.store.*" })
@SpringBootApplication
@EnableFeignClients
public class EmailSenderProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailSenderProjectApplication.class, args);
    }
}
