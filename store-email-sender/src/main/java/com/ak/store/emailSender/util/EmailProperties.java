package com.ak.store.emailSender.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private Verification verification;

    @Data
    public static class Verification {
        private String url;
        private String subject;
        private String content;
    }
}
