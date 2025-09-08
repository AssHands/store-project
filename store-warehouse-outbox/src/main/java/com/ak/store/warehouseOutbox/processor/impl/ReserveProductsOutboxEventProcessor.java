package com.ak.store.warehouseOutbox.processor.impl;


import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaResponseEvent;
import com.ak.store.warehouseOutbox.model.OutboxEvent;
import com.ak.store.warehouseOutbox.model.OutboxEventStatus;
import com.ak.store.warehouseOutbox.model.OutboxEventType;
import com.ak.store.warehouseOutbox.processor.OutboxEventProcessor;
import com.ak.store.warehouseOutbox.service.OutboxEventService;
import com.ak.store.warehouseOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveProductsOutboxEventProcessor implements OutboxEventProcessor {
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
        return OutboxEventType.RESERVE_PRODUCTS;
    }
}