package com.ak.store.orderSagaWorker.model.entity;

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

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxEventType type;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;

    private LocalDateTime retryTime;
}