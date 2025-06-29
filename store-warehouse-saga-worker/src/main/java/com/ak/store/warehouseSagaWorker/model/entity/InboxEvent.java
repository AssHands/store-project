package com.ak.store.warehouseSagaWorker.model.entity;

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
@Table(name = "inbox")
public class InboxEvent {
    @Id
    private UUID id;

    private String stepName;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Enumerated(EnumType.STRING)
    private InboxEventType type;

    @Enumerated(EnumType.STRING)
    private InboxEventStatus status;

    private LocalDateTime retryTime;
}