package com.ak.store.sagaOrchestrator.processor;

import com.ak.store.common.kafka.KafkaEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.kafka.producer.EventProducerKafka;
import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.mapper.JsonMapper;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaProcessor {
    private final SagaProperties sagaProperties;
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapper jsonMapper;

    public boolean handleSagaStep(SagaStep sagaStep) {
        if(sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName()) == null) {
            return sendLastStep(sagaStep);
        }

        return sendStep(sagaStep);
    }

    private boolean sendLastStep(SagaStep sagaStep) {
        var sagaDef = sagaProperties.getDefinition(sagaStep.getName());

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
                .sagaName(sagaStep.getSaga().getName())
                .stepId(sagaStep.getId())
                .stepName(stepName)
                .build();

        return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private boolean sendStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName());

        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();

        if(sagaStep.getIsCompensation()) {
            topic = stepDef.getTopics().getCompensationRequest();
        }

        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return false;
        }

        var requestEvent = SagaRequestEvent.builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .sagaName(sagaStep.getSaga().getName())
                .stepId(sagaStep.getId())
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