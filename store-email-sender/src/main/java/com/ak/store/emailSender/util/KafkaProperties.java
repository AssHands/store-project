package com.ak.store.emailSender.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String groupId;

    private String dltPrefix;

    private Map<String, String> topics = new HashMap<>();

    public String getTopicByKey(String key) {
        return topics.get(key);
    }
}
