package com.ak.store.search.model.document;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("characteristic")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicDocument {
    private Long id;

    private String name;

    private Boolean isText;

    private List<RangeValueDocument> rangeValues = new ArrayList<>();

    private List<String> textValues = new ArrayList<>();
}