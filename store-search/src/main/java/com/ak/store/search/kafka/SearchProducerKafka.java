package com.ak.store.search.kafka;

import com.ak.store.common.event.search.SearchAllEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProducerKafka {
    private final KafkaTemplate<String, SearchEvent> kafkaProductTemplate;

    public void send(SearchAllEvent searchAllEvent) {
        try {
            SendResult<String, SearchEvent> future = kafkaProductTemplate
                    .send("search-all-events", searchAllEvent).get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("kafka search-events error");
        }
    }
}
