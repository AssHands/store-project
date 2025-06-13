package com.ak.store.sagaOrchestrator.util.saga;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "saga")
public class SagaProperties {
    private Map<String, SagaDefinition> sagaDefinitions;

    @Data
    public static class SagaDefinition {
        private String name;

        private String topic;

        private List<SagaStep> steps;
    }

    @Data
    public static class SagaStep {
        private String name;

        private String topic;

        private String compensationTopic;

        private Integer timeout;
    }

}