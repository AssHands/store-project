package com.ak.store.common.model.order.snapshot;

import com.ak.store.common.model.user.snapshot.UserIdentitySnapshot;
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
public class OrderCreatedSnapshotPayload {
    private OrderSnapshot order;

    private UserIdentitySnapshot userIdentity;
}
