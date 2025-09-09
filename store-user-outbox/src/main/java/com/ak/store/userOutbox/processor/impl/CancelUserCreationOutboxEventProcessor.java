package com.ak.store.userOutbox.processor.impl;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaResponseEvent;
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
public class CancelUserCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());
        var response = jsonMapperKafka.fromJson(event.getPayload(), SagaResponseEvent.class);

        eventProducerKafka.sendAsync(response, topic, event.getId().toString())
                .thenRun(() -> outboxEventService.markOneAs(event, OutboxEventStatus.COMPLETED));
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CANCEL_USER_CREATION;
    }
}
