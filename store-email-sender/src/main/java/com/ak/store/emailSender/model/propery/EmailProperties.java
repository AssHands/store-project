package com.ak.store.emailSender.model.propery;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private Verify verify;
    private Order order;

    @Data
    public static class Verify {
        private String url;
        private String subject;
        private String content;
    }

    @Data
    public static class Order {
        private String subject;
        private String content;
    }
}
