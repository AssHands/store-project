package com.ak.store.kafka.storekafkastarter.model.event.user;

import com.ak.store.kafka.storekafkastarter.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserVerificationSnapshot;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserVerificationEvent implements KafkaEvent {
    private UUID eventId;

    private UserVerificationSnapshot request;
}
