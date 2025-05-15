package com.ak.store.common.event.catalogue;

import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
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
public class CategoryCreatedEvent implements CategoryEvent {
    private UUID taskId;
    private CategorySnapshotPayload category;
}
