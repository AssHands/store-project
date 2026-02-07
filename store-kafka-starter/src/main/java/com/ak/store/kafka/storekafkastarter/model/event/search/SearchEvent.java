package com.ak.store.kafka.storekafkastarter.model.event.search;

import com.ak.store.kafka.storekafkastarter.model.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.search.SearchSnapshot;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchEvent implements KafkaEvent {
    private UUID eventId;

    private SearchSnapshot searchData;
}
