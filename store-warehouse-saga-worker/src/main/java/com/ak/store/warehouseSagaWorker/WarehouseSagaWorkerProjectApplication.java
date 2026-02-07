package com.ak.store.warehouseSagaWorker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ak.store.*")
public class WarehouseSagaWorkerProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseSagaWorkerProjectApplication.class, args);
    }
}