package com.ak.store.paymentOutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.ak.store.*" })
@EnableScheduling
@SpringBootApplication
public class PaymentOutboxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentOutboxProjectApplication.class, args);
    }
}