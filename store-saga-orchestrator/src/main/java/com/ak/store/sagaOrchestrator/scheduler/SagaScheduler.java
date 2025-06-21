package com.ak.store.sagaOrchestrator.scheduler;

import com.ak.store.common.saga.orderCreation.event.order.OrderCreationRequestEvent;
import com.ak.store.common.saga.orderCreation.pojo.order.OrderCreationRequest;
import com.ak.store.sagaOrchestrator.kafka.producer.EventProducerKafka;
import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SagaScheduler {
    private final SagaStepService sagaStepService;
    private final SagaService sagaService;
    private final SagaProperties sagaProperties;
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing();
        processAllStep(sagaSteps);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeSaga() {
        var sagas = sagaService.findAllForProcessing();
        processAllSaga(sagas);
    }

    private void processAllStep(List<SagaStep> sagaSteps) {
        for(var sagaStep : sagaSteps) {
            if(sagaStep.getIsCompensation()) {
                processCompensation(sagaStep);
            } else {
                processSuccess(sagaStep);
            }
        }

        //todo
        sagaStepService.markAllAsCompleted(sagaSteps);
    }

    private void processAllSaga(List<Saga> sagas) {
        for(var saga : sagas) {
            processSaga(saga);
        }

        //todo
        sagaService.markAllAsInProgress(sagas);
    }

    private void processSaga(Saga saga) {
        var stepProperty = sagaProperties.getFirstStep(saga.getName());

        if(stepProperty == null) {
            //todo
            System.out.println("nul");
            return;
        }

        String stepName = stepProperty.getName();
        String topic = stepProperty.getTopics().getRequest();

        var requestEvent = OrderCreationRequestEvent.builder()
                .request(gson.fromJson(saga.getPayload(), OrderCreationRequest.class))
                .build();

        requestEvent.setSagaId(saga.getId());
        requestEvent.setStepName(stepName);

        eventProducerKafka.send(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private void processSuccess(SagaStep sagaStep) {
        var stepProperty = sagaProperties.getNextStep(sagaStep.getSaga().getName(), sagaStep.getName());

        if(stepProperty == null) {
            //todo
            System.out.println("nul");
            return;
        }

        String stepName = stepProperty.getName();
        String topic = stepProperty.getTopics().getRequest();

        var requestEvent = OrderCreationRequestEvent.builder()
                .request(gson.fromJson(sagaStep.getSaga().getPayload(), OrderCreationRequest.class))
                .build();

        requestEvent.setSagaId(sagaStep.getSaga().getId());
        requestEvent.setStepName(stepName);

        eventProducerKafka.send(requestEvent, topic, requestEvent.getSagaId().toString());
    }

    private void processCompensation(SagaStep sagaStep) {
        var stepProperty = sagaProperties.getPreviousStep(sagaStep.getSaga().getName(), sagaStep.getName());

        if(stepProperty == null) {
            //todo
            System.out.println("nul");
            return;
        }

        String stepName = stepProperty.getName();
        String topic = stepProperty.getTopics().getCompensationRequest();

        var requestEvent = OrderCreationRequestEvent.builder()
                .request(gson.fromJson(sagaStep.getSaga().getPayload(), OrderCreationRequest.class))
                .build();

        requestEvent.setSagaId(sagaStep.getSaga().getId());
        requestEvent.setStepName(stepName);

        eventProducerKafka.send(requestEvent, topic, requestEvent.getSagaId().toString());
    }
}