package com.ak.store.emailSender.inbox;

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

    @Column(updatable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private InboxEventType type;

    @Enumerated(EnumType.STRING)
    private InboxEventStatus status;

    private LocalDateTime retryTime;
}