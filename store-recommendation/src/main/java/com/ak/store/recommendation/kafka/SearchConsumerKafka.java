package com.ak.store.recommendation.kafka;

import com.ak.store.kafka.storekafkastarter.model.event.search.SearchEvent;
import com.ak.store.recommendation.mapper.SearchHistoryMapper;
import com.ak.store.recommendation.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchConsumerKafka {
    private final SearchHistoryService searchHistoryService;
    private final SearchHistoryMapper searchHistoryMapper;

    @KafkaListener(topics = "${kafka.topics.search-events}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleSearchEvent(List<SearchEvent> searchEvents) {
        searchHistoryService.putAll(searchEvents.stream()
                .map(searchHistoryMapper::toWriteCommand)
                .toList());
    }
}