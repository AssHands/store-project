package com.ak.store.paymentOutbox.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//todo убрать аннотацию @Configuration. добавить инфу про этот бин в project application класс
@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String dltPrefix;

    private Map<String, String> topics = new HashMap<>();

    public String getTopicByKey(String key) {
        return topics.get(key);
    }
}
