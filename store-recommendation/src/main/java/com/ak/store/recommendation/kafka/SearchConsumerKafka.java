package com.ak.store.recommendation.kafka;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.recommendation.facade.RecommendationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConsumerKafka {
    private final RecommendationFacade recommendationFacade;

    @KafkaListener(topics = "${kafka.topics.search-all-events}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<SearchAllEvent> searchAllEvents) {
        recommendationFacade.putInSearchHistory(searchAllEvents);
    }
}