package com.ak.store.userOutbox.processor.impl;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserSnapshot;
import com.ak.store.userOutbox.model.OutboxEvent;
import com.ak.store.userOutbox.model.OutboxEventStatus;
import com.ak.store.userOutbox.model.OutboxEventType;
import com.ak.store.userOutbox.processor.OutboxEventProcessor;
import com.ak.store.userOutbox.service.OutboxEventService;
import com.ak.store.userOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRegistrationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());
        var snapshot = jsonMapperKafka.fromJson(event.getPayload(), UserSnapshot.class);

        var request = SagaRequestEvent.<UserSnapshot>builder()
                .sagaId(event.getId())
                .request(snapshot)
                .build();

        eventProducerKafka.sendAsync(request, topic, event.getId().toString())
                .thenRun(() -> outboxEventService.markOneAs(event, OutboxEventStatus.COMPLETED));
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.USER_REGISTRATION;
    }
}