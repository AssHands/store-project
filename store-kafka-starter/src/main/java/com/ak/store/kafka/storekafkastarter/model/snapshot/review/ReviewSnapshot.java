package com.ak.store.kafka.storekafkastarter.model.snapshot.review;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewSnapshot {
    private String id;

    private UUID userId;

    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    private Integer grade;

    private LocalDateTime time;
}