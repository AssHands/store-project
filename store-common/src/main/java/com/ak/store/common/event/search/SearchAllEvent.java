package com.ak.store.common.event.search;

import com.ak.store.common.model.search.dto.ConsumerSearchDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchAllEvent implements SearchEvent {
    private ConsumerSearchDTO consumerSearch;
}
