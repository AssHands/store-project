package com.ak.store.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class PaymentProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProjectApplication.class, args);
    }
}