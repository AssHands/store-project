package com.ak.store.paymentOutbox.processor.impl;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.paymentOutbox.model.OutboxEvent;
import com.ak.store.paymentOutbox.model.OutboxEventType;
import com.ak.store.paymentOutbox.processor.OutboxEventProcessor;
import com.ak.store.paymentOutbox.service.OutboxEventService;
import com.ak.store.paymentOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseFundsOutboxEventProcessor implements OutboxEventProcessor {
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
        return OutboxEventType.RELEASE_FUNDS;
    }
}