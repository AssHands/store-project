package com.ak.store.sagaOrchestrator.processor;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.sagaOrchestrator.mapper.JsonMapper;
import com.ak.store.sagaOrchestrator.model.SagaStep;
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

    public void handleSagaStep(SagaStep sagaStep) {
        if (sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName()) == null) {
            sendLastStep(sagaStep);
            return;
        }

        sendStep(sagaStep);
    }

    private void sendLastStep(SagaStep sagaStep) {
        var sagaDef = sagaProperties.getDefinition(sagaStep.getName());

        String stepName = sagaDef.getName();
        String topic = sagaDef.getCompensationRequestTopic();
        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return;
        }

        var event = SagaRequestEvent.<JsonNode>builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .sagaName(sagaStep.getSaga().getName())
                .stepId(sagaStep.getId())
                .stepName(stepName)
                .build();

        eventProducerKafka.sendAsync(event, topic, event.getSagaId().toString());
    }

    private void sendStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName());

        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();

        if (sagaStep.getIsCompensation()) {
            topic = stepDef.getTopics().getCompensationRequest();
        }

        JsonNode request;

        try {
            request = jsonMapper.toJsonNode(sagaStep.getSaga().getPayload());
        } catch (JsonProcessingException e) {
            return;
        }

        var event = SagaRequestEvent.builder()
                .request(request)
                .sagaId(sagaStep.getSaga().getId())
                .sagaName(sagaStep.getSaga().getName())
                .stepId(sagaStep.getId())
                .stepName(stepName)
                .build();

        eventProducerKafka.sendAsync(event, topic, event.getSagaId().toString());
    }
}