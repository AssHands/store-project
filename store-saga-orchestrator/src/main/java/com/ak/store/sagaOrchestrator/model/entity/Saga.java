package com.ak.store.sagaOrchestrator.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Saga {
    @Id
    private UUID id;

    private String sagaName;

    private String payload;

    @OneToMany
    private List<SagaStep> steps = new ArrayList<>();

    private LocalDateTime time;
}