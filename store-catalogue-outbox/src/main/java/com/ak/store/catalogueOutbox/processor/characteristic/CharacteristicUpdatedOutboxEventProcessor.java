package com.ak.store.catalogueOutbox.processor.characteristic;

import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventStatus;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.catalogueOutbox.service.OutboxEventService;
import com.ak.store.catalogueOutbox.util.KafkaTopicRegistry;
import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicUpdatedEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.characteristic.CharacteristicPayloadSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());

        var message = new CharacteristicUpdatedEvent(event.getId(),
                jsonMapperKafka.fromJson(event.getPayload(), CharacteristicPayloadSnapshot.class));

        String key = message.getPayload().getCharacteristic().getId().toString();

        eventProducerKafka.sendAsync(message, topic, key)
                .thenRun(() -> outboxEventService.markOneAs(event, OutboxEventStatus.COMPLETED));
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CHARACTERISTIC_UPDATED;
    }
}
