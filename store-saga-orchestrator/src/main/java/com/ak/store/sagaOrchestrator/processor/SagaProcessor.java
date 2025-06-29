package com.ak.store.sagaOrchestrator.processor;

import com.ak.store.common.kafka.KafkaEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.kafka.producer.EventProducerKafka;
import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.mapper.JsonMapper;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SagaProcessor {
    private final SagaProperties sagaProperties;
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapper jsonMapper;

    public boolean handleSaga(Saga saga) {
        var stepDef = sagaProperties.getFirstStep(saga.getName());

        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(saga.getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(saga.getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, saga.getId().toString());
    }

    public boolean handleFailedSaga(Saga saga) {
        var sagaDef = sagaProperties.getDefinition(saga.getName());

        String stepName = sagaDef.getName();
        String topic = sagaDef.getCompensationRequestTopic();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(saga.getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(saga.getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, saga.getId().toString());
    }

    public boolean handleSagaStep(SagaStep sagaStep) {
        return sagaStep.getIsCompensation()
                ? handleCompensationStep(sagaStep)
                : handleStep(sagaStep);
    }

    private boolean handleStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getNextStep(sagaStep.getSaga().getName(), sagaStep.getName());

        return stepDef == null
                ? sendStep(sagaStep, sagaProperties.getLastStep(sagaStep.getSaga().getName()))
                : sendStep(sagaStep, sagaProperties.getNextStep(sagaStep.getSaga().getName(), sagaStep.getName()));
    }

    private boolean handleCompensationStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getPreviousStep(sagaStep.getSaga().getName(), sagaStep.getName());

        return stepDef == null
                ? sendLastCompensationStep(sagaStep, sagaProperties.getDefinition(sagaStep.getSaga().getName()))
                : sendCompensationStep(sagaStep, sagaProperties.getPreviousStep(sagaStep.getSaga().getName(), sagaStep.getName()));
    }

    private boolean sendStep(SagaStep sagaStep, SagaProperties.SagaStepDefinition stepDef) {
        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private boolean sendCompensationStep(SagaStep sagaStep, SagaProperties.SagaStepDefinition stepDef) {
        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getCompensationRequest();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private boolean sendLastCompensationStep(SagaStep sagaStep, SagaProperties.SagaDefinition sagaDef) {
        String stepName = sagaDef.getName();
        String topic = sagaDef.getCompensationRequestTopic();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private <T extends KafkaEvent> boolean sendEvent(T payload, String topic, String key) {
        try {
            eventProducerKafka.send(payload, topic, key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}