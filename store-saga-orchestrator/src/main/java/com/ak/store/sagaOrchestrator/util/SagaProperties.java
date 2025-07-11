package com.ak.store.sagaOrchestrator.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Data
@Configuration
@ConfigurationProperties(prefix = "saga")
public class SagaProperties {
    private Map<String, SagaDefinition> definitions;

    private List<String> allRequestSagaTopics = new ArrayList<>();
    private List<String> allResponseStepTopics = new ArrayList<>();
    private List<String> allResponseCompensationStepTopics = new ArrayList<>();

    @PostConstruct
    public void init() {
        allRequestSagaTopics = definitions.values().stream()
                .map(SagaDefinition::getRequestTopic)
                .toList();

        allResponseStepTopics = definitions.values().stream()
                .map(SagaDefinition::getSteps)
                .flatMap(stepMap -> stepMap.values().stream())
                .map(SagaStepDefinition::getTopics)
                .flatMap(step -> Stream.of(
                        step.getResponse()))
                .toList();

        allResponseCompensationStepTopics = definitions.values().stream()
                .map(SagaDefinition::getSteps)
                .flatMap(stepMap -> stepMap.values().stream())
                .map(SagaStepDefinition::getTopics)
                .flatMap(step -> Stream.of(
                        step.getCompensationResponse()))
                .toList();
        System.out.println();
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

    public SagaStepDefinition getLastStep(String definition) {
        var stepsIterator = definitions.get(definition).getSteps().entrySet().iterator();
        SagaStepDefinition lastStep = null;

        while (stepsIterator.hasNext()) {
            lastStep = stepsIterator.next().getValue();
        }

        return lastStep;
    }

    public SagaDefinition getDefinition(String name) {
        return definitions.get(name);
    }

    public SagaDefinition getDefinitionByRequestTopic(String topic) {
        return definitions.values().stream()
                .filter(v -> v.getRequestTopic().equals(topic))
                .findFirst()
                .orElse(null);
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

        private String compensationRequestTopic;

        private String compensationResponseTopic;

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