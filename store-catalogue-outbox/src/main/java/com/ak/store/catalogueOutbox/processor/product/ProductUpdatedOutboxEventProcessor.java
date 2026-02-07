package com.ak.store.catalogueOutbox.processor.product;

import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventStatus;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.catalogueOutbox.service.OutboxEventService;
import com.ak.store.catalogueOutbox.util.KafkaTopicRegistry;
import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductUpdatedEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());

        var message = new ProductUpdatedEvent(event.getId(),
                jsonMapperKafka.fromJson(event.getPayload(), ProductSnapshotPayload.class));

        String key = message.getPayload().getProduct().getId().toString();

        eventProducerKafka.sendAsync(message, topic, key)
                .thenRun(() -> outboxEventService.markOneAs(event, OutboxEventStatus.COMPLETED));
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_UPDATED;
    }
}
