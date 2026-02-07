package com.ak.store.paymentOutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class PaymentOutboxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentOutboxProjectApplication.class, args);
    }
}