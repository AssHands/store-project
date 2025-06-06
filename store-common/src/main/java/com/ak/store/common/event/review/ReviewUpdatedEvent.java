package com.ak.store.common.event.review;

import com.ak.store.common.event.KafkaEvent;
import com.ak.store.common.model.review.snapshot.ReviewUpdatedSnapshotPayload;
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
public class ReviewUpdatedEvent implements KafkaEvent {
    private UUID eventId;

    private ReviewUpdatedSnapshotPayload payload;
}