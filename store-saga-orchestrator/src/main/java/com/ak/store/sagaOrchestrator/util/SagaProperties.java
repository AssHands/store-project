package com.ak.store.sagaOrchestrator.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Stream;

@Data
@Configuration
@ConfigurationProperties(prefix = "saga")
public class SagaProperties {
    private Map<String, SagaDefinition> definitions;
    private List<String> allTopics = new ArrayList<>();

    @PostConstruct
    public void init() {
        allTopics = definitions.values().stream()
                .flatMap(def -> {
                    Stream<String> listenTopicStream = Stream.of(
                            def.getRequestTopic(),
                            def.getResponseTopic()
                    ).filter(Objects::nonNull);

                    Stream<String> stepTopicsStream = def.getSteps().values().stream()
                            .flatMap(step -> Stream.ofNullable(step.getTopics()))
                            .flatMap(topics -> Stream.of(
                                    topics.getRequest(),
                                    topics.getResponse(),
                                    topics.getCompensationRequest(),
                                    topics.getCompensationResponse()
                            ))
                            .filter(Objects::nonNull);

                    return Stream.concat(listenTopicStream, stepTopicsStream);
                })
                .distinct()
                .toList();
    }

    public SagaStepDefinition getNextStep(String definition, String currentStep) {
        var steps = definitions.get(definition).getSteps();

        return steps.entrySet().stream()
                .dropWhile(entry -> !entry.getValue().name.equals(currentStep))
                .skip(1)
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public SagaStepDefinition getPreviousStep(String definition, String currentStep) {
        var steps = definitions.get(definition).getSteps();

        return steps.entrySet().stream()
                .takeWhile(entry -> !entry.getValue().name.equals(currentStep))
                .reduce((first, second) -> second)
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public SagaStepDefinition getFirstStep(String definition) {
        var steps = definitions.get(definition).getSteps();
        return steps.isEmpty() ? null : steps.values().iterator().next();
    }

    public SagaDefinition getDefinition(String name) {
        return definitions.get(name);
    }

    public SagaStepDefinition getCurrentStep(String definition, String currentStep) {
        var steps = definitions.get(definition).getSteps();

        return steps.entrySet().stream()
                .dropWhile(entry -> !entry.getKey().equals(currentStep))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }


    @Data
    public static class SagaDefinition {
        private String name;

        private String requestTopic;

        private String responseTopic;

        private Integer timeout;

        private LinkedHashMap<String, SagaStepDefinition> steps;
    }

    @Data
    public static class SagaStepDefinition {
        private String name;

        private Integer timeout;

        private SagaStepTopicsDefinition topics;
    }

    @Data
    public static class SagaStepTopicsDefinition {
        private String request;

        private String response;

        private String compensationRequest;

        private String compensationResponse;
    }
}