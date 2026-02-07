package com.ak.store.reviewOutbox.processor.updated;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewUpdatedSnapshot;
import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventStatus;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.ak.store.reviewOutbox.service.OutboxEventService;
import com.ak.store.reviewOutbox.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final JsonMapperKafka jsonMapperKafka;
    private final KafkaTopicRegistry kafkaTopicRegistry;
    private final OutboxEventService outboxEventService;
    @Override
    public void process(OutboxEvent event) {
        String topic = kafkaTopicRegistry.getTopicByEvent(getType());
        var snapshot = jsonMapperKafka.fromJson(event.getPayload(), ReviewUpdatedSnapshot.class);

        var request = SagaRequestEvent.<ReviewUpdatedSnapshot>builder()
                .sagaId(event.getId())
                .request(snapshot)
                .build();

        eventProducerKafka.sendAsync(request, topic, event.getId().toString())
                .thenRun(() -> outboxEventService.markOneAs(event, OutboxEventStatus.COMPLETED));
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.REVIEW_UPDATED;
    }
}
