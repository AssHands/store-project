package com.ak.store.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ak.store.*" })
@SpringBootApplication
public class SearchProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchProjectApplication.class, args);
    }
}