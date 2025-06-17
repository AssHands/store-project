package com.ak.store.sagaOrchestrator.repository;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SagaRepo extends JpaRepository<Saga, UUID> {
}
