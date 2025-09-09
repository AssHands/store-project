package com.ak.store.SynchronizationOutbox.processor.impl;

import com.ak.store.SynchronizationOutbox.model.OutboxEvent;
import com.ak.store.SynchronizationOutbox.model.OutboxEventStatus;
import com.ak.store.SynchronizationOutbox.model.OutboxEventType;
import com.ak.store.SynchronizationOutbox.processor.OutboxEventProcessor;
import com.ak.store.SynchronizationOutbox.service.OutboxEventService;
import com.ak.store.SynchronizationOutbox.util.KafkaTopicRegistry;
import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductSynchronizationOutboxEventProcessor implements OutboxEventProcessor {
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
        return OutboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION;
    }
}
