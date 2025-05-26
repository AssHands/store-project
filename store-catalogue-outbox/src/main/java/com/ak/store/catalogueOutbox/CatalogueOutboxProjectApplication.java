package com.ak.store.catalogueOutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories("com.ak.store.*")
@ComponentScan(basePackages = {"com.ak.store.*" })
@EntityScan("com.ak.store.*")
@EnableScheduling
@SpringBootApplication
public class CatalogueOutboxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogueOutboxProjectApplication.class, args);
    }
}