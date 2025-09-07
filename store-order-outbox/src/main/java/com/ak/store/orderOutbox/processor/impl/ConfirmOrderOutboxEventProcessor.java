package com.ak.store.orderOutbox.processor.impl;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.ak.store.orderOutbox.service.OutboxEventService;
import com.ak.store.orderOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmOrderOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());
        var response = jsonMapperKafka.fromJson(event.getPayload(), SagaResponseEvent.class);

        eventProducerKafka.sendAsync(response, topic, event.getId().toString())
                .thenRun(() -> outboxEventService.markOneAsCompleted(event));

    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CONFIRM_ORDER;
    }
}
