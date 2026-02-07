package com.ak.store.search.kafka;

import com.ak.store.kafka.storekafkastarter.EventProducerKafka;
import com.ak.store.kafka.storekafkastarter.model.event.search.SearchEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.search.SearchSnapshot;
import com.ak.store.search.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTopicRegistry topicRegistry;
    private final EventProducerKafka eventProducerKafka;

    public void produceSearchEvent(UUID userId, Long categoryId) {
        var event = SearchEvent.builder()
                .eventId(UUID.randomUUID())
                .searchData(SearchSnapshot.builder()
                        .userId(userId)
                        .categoryId(categoryId)
                        .build())
                .build();

        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        eventProducerKafka.sendAsync(event, topic, userId.toString());
    }
}
