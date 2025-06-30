package com.ak.store.sagaOrchestrator.kafka.consumer;

import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class SagaConsumerKafka {
    private final SagaService sagaService;
    private final SagaStepService sagaStepService;
    private final SagaProperties sagaProperties;

    @KafkaListener(
            topics = "#{@sagaProperties.getAllRequestSagaTopics()}",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handle(List<SagaRequestEvent> events,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics, Acknowledgment ack) {
        for (int i = 0; i < events.size(); i++) {
            var event = events.get(i);
            var topic = topics.get(i);
            String sagaName = sagaProperties.getDefinitionByRequestTopic(topic).getName();

            try {
                sagaService.createOne(event.getSagaId(), sagaName, event.getRequest().toString());
            } catch (Exception e) {
                //todo не получилось создать запись в бд о новой саге. что делать? кидать в dlt топик и создавать новую failed сагу?
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "#{@sagaProperties.getAllResponseStepTopics()}",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleResponse(List<SagaResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                if (event.getStatus() == SagaResponseStatus.SUCCESS) {
                    sagaStepService.continueOne(event.getSagaId(), event.getStepName());
                } else {
                    sagaStepService.compensateOne(event.getSagaId(), event.getStepName());
                }
            } catch (Exception e) {
                //todo не получилось создать запись в бд о шаге. что делать? кидать в dlt топик и закрывать сагу?
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "#{@sagaProperties.getAllResponseCompensationStepTopics()}",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCompensation(List<SagaResponseEvent> events, Acknowledgment ack) {
        //todo а что если компенсация вернет FAILURE статус?
        List<UUID> failedSagaIds = new ArrayList<>();

        for (var event : events) {
            if (sagaProperties.getDefinition(event.getStepName()) == null) {
                sagaStepService.compensateOne(event.getSagaId(), event.getStepName());
            } else {
                failedSagaIds.add(event.getSagaId());
            }
        }

        sagaService.markAllAsByIds(failedSagaIds, SagaStatus.COMPLETED);
        ack.acknowledge();
    }
}