package com.ak.store.sagaOrchestrator.kafka;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaRequestEvent;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseStatus;
import com.ak.store.sagaOrchestrator.facade.SagaFacade;
import com.ak.store.sagaOrchestrator.facade.SagaStepFacade;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SagaConsumerKafka {
    private final SagaProperties sagaProperties;
    private final SagaFacade sagaFacade;
    private final SagaStepFacade sagaStepFacade;
    private final JsonMapperKafka jsonMapperKafka;

    @KafkaListener(
            topics = "#{@sagaProperties.getAllRequestSagaTopics()}",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleStartSaga(List<SagaRequestEvent> events,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics, Acknowledgment ack) {
        for (int i = 0; i < events.size(); i++) {
            var event = events.get(i);
            var topic = topics.get(i);
            String sagaName = sagaProperties.getDefinitionByRequestTopic(topic).getName();

            try {
                sagaFacade.createOne(event.getSagaId(), sagaName, jsonMapperKafka.toJson(event.getRequest()));
            } catch (Exception e) {
                System.out.println("a");
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
    public void handleSteps(List<SagaResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                if (event.getStatus() == SagaResponseStatus.SUCCESS) {
                    sagaStepFacade.continueOne(event.getSagaId(), event.getSagaName(), event.getStepId(), event.getStepName());
                } else {
                    sagaStepFacade.startCompensation(event.getSagaId(), event.getSagaName(), event.getStepId(), event.getStepName());
                }
            } catch (Exception e) {
                System.out.println("a");
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
    public void handleCompensationSteps(List<SagaResponseEvent> events, Acknowledgment ack) {
        for (var event : events) {
            //todo а что если компенсация вернет FAILURE статус?
            sagaStepFacade.compensateOne(event.getSagaId(), event.getSagaName(), event.getStepId(), event.getStepName());
        }

        ack.acknowledge();
    }
}