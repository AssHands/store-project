package com.ak.store.common.kafka.search;

import com.ak.store.common.kafka.KafkaEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchOneEvent implements KafkaEvent {
    //private ConsumerSearchDTO consumerSearch;
}
