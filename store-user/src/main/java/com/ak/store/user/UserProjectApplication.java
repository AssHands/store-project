package com.ak.store.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.ak.store.*")
@ComponentScan(basePackages = {"com.ak.store.*" })
@EntityScan("com.ak.store.*")
@SpringBootApplication
public class UserProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProjectApplication.class, args);
    }
}