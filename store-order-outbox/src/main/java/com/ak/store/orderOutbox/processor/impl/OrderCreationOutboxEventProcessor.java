package com.ak.store.orderOutbox.processor.impl;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.order.OrderCreation;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaRequestEvent;
import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.ak.store.orderOutbox.service.OutboxEventService;
import com.ak.store.orderOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());
        var orderCreation = jsonMapperKafka.fromJson(event.getPayload(), OrderCreation.class);

        var request = SagaRequestEvent.<OrderCreation>builder()
                .sagaId(event.getId())
                .request(orderCreation)
                .build();

        eventProducerKafka.sendAsync(request, topic, event.getId().toString())
                .thenRun(() -> outboxEventService.markOneAsCompleted(event));

    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.ORDER_CREATION;
    }
}