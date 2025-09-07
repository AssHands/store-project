package com.ak.store.paymentSagaWorker.model.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    private UUID id;

    @Column(updatable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private OutboxEventType type;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;

    private LocalDateTime retryTime;
}