package com.ak.store.sagaOrchestrator.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "saga_step",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_saga_step_name_compensation_saga",
                        columnNames = {"name", "is_compensation", "saga_id"}
                )
        })
public class SagaStep {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Boolean isCompensation;

    @Enumerated(EnumType.STRING)
    private SagaStepStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saga_id")
    private Saga saga;

    private LocalDateTime time;

    private LocalDateTime retryTime;
}