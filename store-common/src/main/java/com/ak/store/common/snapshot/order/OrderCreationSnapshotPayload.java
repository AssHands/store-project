package com.ak.store.common.snapshot.order;

import com.ak.store.common.snapshot.user.UserIdentitySnapshot;
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
public class OrderCreationSnapshotPayload {
    private OrderSnapshot order;

    private UserIdentitySnapshot userIdentity;
}