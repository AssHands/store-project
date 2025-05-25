package com.ak.store.outboxScheduler.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private Map<String, String> topics = new HashMap<>();

    public String getTopicByKey(String key) {
        return topics.get(key);
    }
}
