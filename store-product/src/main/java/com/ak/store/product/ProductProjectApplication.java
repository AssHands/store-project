package com.ak.store.product;

import com.ak.store.filterQuery.FilterQueryGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductProjectApplication.class, args);
    }

    @Bean
    public FilterQueryGenerator filterQueryGenerator() {
        return new FilterQueryGenerator("product_new",
                "jsonb_extract_path_text(properties,",
                ")::integer");
    }
}