package com.ak.store.search.model.document;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("category")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryDocument {
    private Long id;
    private String name;
    private Long parentId;

    @Builder.Default
    private List<Long> characteristics = new ArrayList<>();
}
