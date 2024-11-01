package com.ak.store.product;

import com.ak.store.queryGenerator.SelectQueryGenerator;
import com.ak.store.queryGenerator.UpdateQueryGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories("com.ak.store.*")
@ComponentScan(basePackages = {"com.ak.store.*" })
@EntityScan("com.ak.store.*")
@SpringBootApplication
public class ProductProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductProjectApplication.class, args);
    }

    @Bean
    public SelectQueryGenerator selectQueryGenerator() {
        return new SelectQueryGenerator("product_new",
                "jsonb_extract_path_text(properties, '",
                "')::integer");
    }

    @Bean
    public UpdateQueryGenerator<Long> updateQueryGenerator() {
        return new UpdateQueryGenerator<>("product_new",
                "",
                "");
    }
}