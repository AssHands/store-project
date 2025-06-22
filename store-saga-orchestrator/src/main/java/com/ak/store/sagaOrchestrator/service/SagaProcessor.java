package com.ak.store.sagaOrchestrator.service;

import com.ak.store.common.event.KafkaEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.kafka.producer.EventProducerKafka;
import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.util.JsonMapper;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class SagaProcessor {
    private final SagaProperties sagaProperties;
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapper jsonMapper;

    public boolean startSaga(Saga saga) {
        var stepDef = sagaProperties.getFirstStep(saga.getName());

        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();

        try {
            var requestEvent = SagaRequestEvent.builder()
                    .request(jsonMapper.toJsonNode(saga.getPayload()))
                    .sagaId(saga.getId())
                    .stepName(stepName)
                    .build();

            return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
        } catch (IOException e) {
            return false;
        }
    }

    public boolean endSaga(Saga saga) {
        var stepDef = sagaProperties.getDefinition(saga.getName());

        String stepName = stepDef.getName();
        String topic = stepDef.getResponseTopic();

        var response = SagaResponseEvent.builder()
                .sagaId(saga.getId())
                .stepName(stepName)
                .status(SagaResponseStatus.FAILURE)
                .build();

        return sendEvent(response, topic, response.getSagaId().toString());
    }

    public boolean handleSagaStep(SagaStep sagaStep) {
        return sagaStep.getIsCompensation()
                ? handleCompensationStep(sagaStep)
                : handleStep(sagaStep);
    }

    private boolean handleCompensationStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getPreviousStep(sagaStep.getSaga().getName(), sagaStep.getName());

        return stepDef == null
                ? sendLastCompensationStep(sagaStep)
                : sendNextCompensationStep(sagaStep);
    }

    private boolean handleStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getNextStep(sagaStep.getSaga().getName(), sagaStep.getName());

        return stepDef == null
                ? sendLastStep(sagaStep)
                : sendNextStep(sagaStep);
    }

    private boolean sendLastStep(SagaStep sagaStep) {
        var sagaDef = sagaProperties.getDefinition(sagaStep.getSaga().getName());
        String stepName = sagaDef.getName();
        String topic = sagaDef.getResponseTopic();

        var responseEvent = SagaResponseEvent.builder()
                .status(SagaResponseStatus.SUCCESS)
                .sagaId(sagaStep.getSaga().getId())
                .stepName(stepName)
                .build();

        return sendEvent(responseEvent, topic, responseEvent.getSagaId().toString());
    }

    private boolean sendLastCompensationStep(SagaStep sagaStep) {
        var sagaDef = sagaProperties.getDefinition(sagaStep.getSaga().getName());
        String stepName = sagaDef.getName();
        String topic = sagaDef.getResponseTopic();

        var responseEvent = SagaResponseEvent.builder()
                .status(SagaResponseStatus.FAILURE)
                .sagaId(sagaStep.getSaga().getId())
                .stepName(stepName)
                .build();

        return sendEvent(responseEvent, topic, responseEvent.getSagaId().toString());
    }

    private boolean sendNextCompensationStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getPreviousStep(sagaStep.getSaga().getName(), sagaStep.getName());
        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getCompensationRequest();

        try {
            var requestEvent = SagaRequestEvent.builder()
                    .request(jsonMapper.toJsonNode(sagaStep.getSaga().getPayload()))
                    .sagaId(sagaStep.getSaga().getId())
                    .stepName(stepName)
                    .build();

            return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
        } catch (IOException e) {
            return false;
        }
    }

    private boolean sendNextStep(SagaStep sagaStep) {
        var stepDef = sagaProperties.getNextStep(sagaStep.getSaga().getName(), sagaStep.getName());
        String stepName = stepDef.getName();
        String topic = stepDef.getTopics().getRequest();

        try {
            var requestEvent = SagaRequestEvent.builder()
                    .request(jsonMapper.toJsonNode(sagaStep.getSaga().getPayload()))
                    .sagaId(sagaStep.getSaga().getId())
                    .stepName(stepName)
                    .build();

            return sendEvent(requestEvent, topic, requestEvent.getSagaId().toString());
        } catch (IOException e) {
            return false;
        }
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