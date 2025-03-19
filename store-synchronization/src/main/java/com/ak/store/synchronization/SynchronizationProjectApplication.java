package com.ak.store.synchronization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ak.store.*" })
@SpringBootApplication
public class SynchronizationProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronizationProjectApplication.class, args);
    }
}
